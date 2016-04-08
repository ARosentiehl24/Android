package com.arrg.android.app.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arrg.android.app.android.R;
import com.arrg.android.app.android.model.Song;
import com.arrg.android.app.android.util.FileUtils;
import com.arrg.android.app.android.view.MusicPlayerActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<Song> songs;

    public SongAdapter(Activity activity, ArrayList<Song> songs) {
        this.activity = activity;
        this.songs = songs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View files = inflater.inflate(R.layout.song_list_row, parent, false);

        return new ViewHolder(files);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Song song = songs.get(position);

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(song.getPathOfFile());

        if (mediaMetadataRetriever.getEmbeddedPicture() == null) {
            Glide.with(activity).load(R.drawable.ic_music_player_default_cover).centerCrop().crossFade().into(holder.photoAlbum);
        } else {
            Glide.with(activity).load(mediaMetadataRetriever.getEmbeddedPicture()).centerCrop().crossFade().into(holder.photoAlbum);
        }

        if (mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) == null) {
            holder.artistName.setText(FileUtils.getFileNameWithoutExtension(song.getPathOfFile()));
        } else {
            holder.artistName.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }

        song.setArtistName(holder.artistName.getText().toString());

        if (mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) == null) {
            holder.nameOfTheSong.setText(R.string.unknown_album);
        } else {
            holder.nameOfTheSong.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        }

        song.setNameOfTheSong(holder.nameOfTheSong.getText().toString());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.photoAlbum)
        ImageView photoAlbum;

        @Bind(R.id.artistName)
        TextView artistName;

        @Bind(R.id.nameOfTheSong)
        TextView nameOfTheSong;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Song song = songs.get(getLayoutPosition());

            MusicPlayerActivity musicPlayerActivity = (MusicPlayerActivity) activity;
            musicPlayerActivity.updateAlbumView(song.getPhotoAlbum(), song.getArtistName(), song.getNameOfTheSong());
        }
    }
}
