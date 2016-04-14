package com.arrg.android.app.android.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.assent.Assent;
import com.afollestad.assent.AssentCallback;
import com.afollestad.assent.PermissionResultSet;
import com.arrg.android.app.android.R;
import com.arrg.android.app.android.adapter.SongAdapter;
import com.arrg.android.app.android.model.Song;
import com.arrg.android.app.android.service.PlaySong;
import com.arrg.android.app.android.util.BitmapUtil;
import com.commit451.nativestackblur.NativeStackBlur;
import com.jaredrummler.fastscrollrecyclerview.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.arrg.android.app.android.Constants.AUDIO_TO_PLAY_EXTRA;

public class MusicPlayerActivity extends AppCompatActivity {

    private ArrayList<Song> songList;
    private PlaySong playSong;
    private SongAdapter songAdapter;

    private int index = 0;
    private static Activity activity;

    @Bind(R.id.bPlayStop)
    ImageButton bPlayStop;

    @Bind(R.id.photoAlbum)
    ImageView photoAlbum;

    @Bind(R.id.miniPhotoAlbum)
    ImageView miniPhotoAlbum;

    @Bind(R.id.songs)
    FastScrollRecyclerView songs;

    @Bind(R.id.artistName)
    TextView artistName;

    @Bind(R.id.nameOfTheSong)
    TextView nameOfTheSong;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @OnClick({R.id.bPreviousTrack, R.id.bPlayStop, R.id.bNextTrack})
    public void OnClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.bPreviousTrack:
                if (index > 0) {
                    int index = this.index - 1;

                    playSong(songAdapter.getSong(index).getPathOfFile(), index);
                }
                break;
            case R.id.bPlayStop:
                if (isPlayServiceRunning()) {
                    playSong = PlaySong.PLAY;

                    if (playSong.isPlaying()) {
                        playSong.onPause();
                        bPlayStop.setImageResource(R.drawable.ic_play_arrow_24dp);
                    } else {
                        playSong.onResume();
                        bPlayStop.setImageResource(R.drawable.ic_pause_24dp);
                    }
                } else {
                    if (songAdapter.getItemCount() > 0) {
                        playSong(songAdapter.getSong(index).getPathOfFile(), index);
                    }
                }
                break;
            case R.id.bNextTrack:
                if (index < songAdapter.getItemCount() - 1) {
                    int index = this.index + 1;

                    playSong(songAdapter.getSong(index).getPathOfFile(), index);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        activity = this;

        Assent.setActivity(this, this);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        songList = new ArrayList<>();

        if (!Assent.isPermissionGranted(Assent.WRITE_EXTERNAL_STORAGE) && !Assent.isPermissionGranted(Assent.RECORD_AUDIO)) {
            Assent.requestPermissions(new AssentCallback() {
                @Override
                public void onPermissionResult(PermissionResultSet result) {
                    if (result.isGranted(Assent.WRITE_EXTERNAL_STORAGE) && result.isGranted(Assent.RECORD_AUDIO)) {
                        load();
                    } else {
                        finish();
                    }
                }
            }, 69, Assent.WRITE_EXTERNAL_STORAGE, Assent.RECORD_AUDIO);
        } else {
            load();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Assent.setActivity(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            Assent.setActivity(this, null);
        }
    }

    @Override
    protected void onDestroy() {
        if (isPlayServiceRunning() || playSong != null) {
            Intent play = new Intent(this, PlaySong.class);
            stopService(play);
        } else {
            Intent play = new Intent(this, PlaySong.class);
            stopService(play);
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Assent.handleResult(permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void load() {
        loadFiles();
        setAdapter();

        //new SearchFilesTask().execute();
    }

    public void updateAlbumView(Bitmap photoAlbum, String artistName, String nameOfTheSong) {
        this.photoAlbum.setImageBitmap(NativeStackBlur.process(photoAlbum, 10));
        this.miniPhotoAlbum.setImageBitmap(photoAlbum);
        this.artistName.setText(artistName);
        this.nameOfTheSong.setText(nameOfTheSong);
    }

    public void playSong(String pathOfFile, int layoutPosition) {
        songAdapter.isPlaying(index, false);

        index = layoutPosition;

        Intent play = new Intent(this, PlaySong.class);
        play.putExtra(AUDIO_TO_PLAY_EXTRA, pathOfFile);

        stopService(play);
        startService(play);

        playSong = PlaySong.PLAY;

        bPlayStop.setImageResource(R.drawable.ic_pause_24dp);

        Song song = songAdapter.getSong(layoutPosition);

        songAdapter.isPlaying(layoutPosition, true);

        updateAlbumView(song.getPhotoAlbum(), song.getArtistName(), song.getNameOfTheSong());
    }

    class SearchFilesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            photoAlbum.setImageBitmap(NativeStackBlur.process(BitmapUtil.getBitmapFromDrawable(photoAlbum.getDrawable()), 10));
        }

        @Override
        protected Void doInBackground(Void... params) {
            long startTime = System.currentTimeMillis();

            loadFiles();

            long endTime = System.currentTimeMillis();

            long duration = (endTime - startTime);

            Log.d("FolderFinish", "Duration: " + TimeUnit.MILLISECONDS.toSeconds(duration) + " seconds" + " - " + duration + " milliseconds");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setAdapter();
        }
    }

    private void loadFiles() {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

        String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";

        Cursor cursor = null;

        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getContentResolver().query(uri, projection, selection, null, sortOrder);

            if (cursor != null) {
                cursor.moveToFirst();

                long startTime = System.currentTimeMillis();

                while (!cursor.isAfterLast()) {
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    //String displayName = cursor.getString(3);
                    //String songDuration = cursor.getString(4);

                    Song song = new Song();

                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(path);

                    byte bitmap[] = mediaMetadataRetriever.getEmbeddedPicture();

                    if (bitmap == null) {
                        song.setPhotoAlbum(BitmapFactory.decodeResource(getResources(), R.drawable.ic_music_player_default_cover));
                    } else {
                        song.setPhotoAlbum(BitmapUtil.getBitmapFromByteArray(bitmap, 0, bitmap.length, 250, 250));
                    }
                    song.setNameOfTheSong(title);
                    song.setArtistName(artist);
                    song.setPathOfFile(path);

                    songList.add(song);

                    Log.d("Folder", title + " - " + artist + " - " + song.getPathOfFile());

                    cursor.moveToNext();
                }

                long endTime = System.currentTimeMillis();

                long duration = (endTime - startTime);

                Log.d("FolderFinish", "Duration: " + TimeUnit.MILLISECONDS.toSeconds(duration) + " seconds" + " - " + duration + " milliseconds");
            }
        } catch (Exception e) {
            Log.e("Folder", e.toString(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void setAdapter() {
        songAdapter = new SongAdapter(activity, songList);

        songs.setHasFixedSize(true);
        songs.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        songs.setAdapter(songAdapter);

        if (songAdapter.getItemCount() > 0) {
            Song song = songAdapter.getSong(0);

            updateAlbumView(song.getPhotoAlbum(), song.getArtistName(), song.getNameOfTheSong());
        }
    }

    private boolean isPlayServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (PlaySong.class.getName().equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }

        return false;
    }
}
