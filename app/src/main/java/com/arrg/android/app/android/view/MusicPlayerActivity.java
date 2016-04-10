package com.arrg.android.app.android.view;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.assent.Assent;
import com.afollestad.assent.AssentCallback;
import com.afollestad.assent.PermissionResultSet;
import com.arrg.android.app.android.R;
import com.arrg.android.app.android.adapter.SongAdapter;
import com.arrg.android.app.android.model.Song;
import com.arrg.android.app.android.util.BitmapUtil;
import com.commit451.nativestackblur.NativeStackBlur;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MusicPlayerActivity extends AppCompatActivity {

    private ArrayList<Song> songList;
    private static Activity activity;

    @Bind(R.id.photoAlbum)
    ImageView photoAlbum;

    @Bind(R.id.miniPhotoAlbum)
    ImageView miniPhotoAlbum;

    @Bind(R.id.songs)
    RecyclerView songs;

    @Bind(R.id.artistName)
    TextView artistName;

    @Bind(R.id.nameOfTheSong)
    TextView nameOfTheSong;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

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

        if (!Assent.isPermissionGranted(Assent.WRITE_EXTERNAL_STORAGE)) {
            Assent.requestPermissions(new AssentCallback() {
                @Override
                public void onPermissionResult(PermissionResultSet result) {
                    if (result.isGranted(Assent.WRITE_EXTERNAL_STORAGE)) {
                        load();
                    } else {
                        finish();
                    }
                }
            }, 69, Assent.WRITE_EXTERNAL_STORAGE);
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
        new SearchFilesTask().execute();
    }

    public void updateAlbumView(Bitmap photoAlbum, String artistName, String nameOfTheSong) {
        this.photoAlbum.setImageBitmap(NativeStackBlur.process(photoAlbum, 5));
        this.miniPhotoAlbum.setImageBitmap(photoAlbum);
        this.artistName.setText(artistName);
        this.nameOfTheSong.setText(nameOfTheSong);
    }

    class SearchFilesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            photoAlbum.setImageBitmap(NativeStackBlur.process(((BitmapDrawable) photoAlbum.getDrawable()).getBitmap(), 5));
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

                    while (!cursor.isAfterLast()) {
                        //MediaData media = new MediaData();
                        String title = cursor.getString(0);
                        String artist = cursor.getString(1);
                        String path = cursor.getString(2);
                        String displayName = cursor.getString(3);
                        String songDuration = cursor.getString(4);

                        Song song = new Song();

                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(path);

                        byte bitmap[] = mediaMetadataRetriever.getEmbeddedPicture();

                        if (bitmap == null) {
                            song.setPhotoAlbum(BitmapFactory.decodeResource(getResources(), R.drawable.ic_music_player_default_cover));
                        } else {
                            song.setPhotoAlbum(BitmapUtil.getBitmapFromByteArray(bitmap, 0, bitmap.length, 100, 100));
                        }
                        song.setNameOfTheSong(title);
                        song.setArtistName(artist);
                        song.setPathOfFile(path);

                        songList.add(song);

                        Log.d("Folder", title + " - " + artist + " - " + song.getPathOfFile());

                        cursor.moveToNext();
                    }
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
            SongAdapter songAdapter = new SongAdapter(activity, songList);

            songs.setAdapter(songAdapter);
            songs.setHasFixedSize(true);
            songs.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

    }
}
