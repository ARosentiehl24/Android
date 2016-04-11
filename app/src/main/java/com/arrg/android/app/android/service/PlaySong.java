package com.arrg.android.app.android.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import static com.arrg.android.app.android.Constants.*;

public class PlaySong extends Service {

    public static PlaySong PLAY;

    private MediaPlayer mediaPlayer;

    public PlaySong() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        PLAY = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String file = intent.getStringExtra(AUDIO_TO_PLAY_EXTRA);

            mediaPlayer = MediaPlayer.create(this, Uri.parse(file));
            mediaPlayer.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onResume() {
        mediaPlayer.start();
    }

    public void onPause() {
        mediaPlayer.pause();
    }

    public void onStop() {
        onDestroy();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();

        super.onDestroy();
    }
}
