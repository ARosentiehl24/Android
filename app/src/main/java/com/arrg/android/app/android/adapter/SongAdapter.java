package com.arrg.android.app.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arrg.android.app.android.R;
import com.arrg.android.app.android.model.Song;
import com.arrg.android.app.android.view.MusicPlayerActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<Song> songs;
    private Song song;

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
        song = songs.get(position);

        holder.nameOfTheSong.setText(song.getNameOfTheSong());
        holder.artistName.setText(song.getArtistName());
        holder.photoAlbum.setImageBitmap(song.getPhotoAlbum());

        if (song.getPlaying()) {
            Log.d("IsPlaying", song.getNameOfTheSong() + " is playing.");

            holder.nameOfTheSong.setTextColor(ContextCompat.getColor(activity, R.color.holo_blue_bright));
            holder.artistName.setTextColor(ContextCompat.getColor(activity, R.color.holo_blue_bright));

        } else {
            holder.nameOfTheSong.setTextColor(ContextCompat.getColor(activity, R.color.background_light));
            holder.artistName.setTextColor(ContextCompat.getColor(activity, R.color.background_light));

        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public Song getSong(int index) {
        return songs.get(index);
    }

    public void isPlaying(int layoutPosition, boolean b) {
        songs.get(layoutPosition).setPlaying(b);

        notifyItemChanged(layoutPosition);
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

            if (song.getPhotoAlbum() == null) {
                Toast.makeText(activity, "The bitmap is null.", Toast.LENGTH_SHORT).show();
            } else {
                MusicPlayerActivity musicPlayerActivity = (MusicPlayerActivity) activity;
                //musicPlayerActivity.updateAlbumView(song.getPhotoAlbum(), song.getArtistName(), song.getNameOfTheSong());
                musicPlayerActivity.playSong(song.getPathOfFile(), getLayoutPosition());
            }
        }
    }
}
