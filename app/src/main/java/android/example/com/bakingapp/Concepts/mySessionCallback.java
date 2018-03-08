package android.example.com.bakingapp.Concepts;

import android.support.v4.media.session.MediaSessionCompat;

import com.google.android.exoplayer2.SimpleExoPlayer;

/**
 * Created by florinel on 08.03.2018.
 */

public class mySessionCallback extends MediaSessionCompat.Callback {
    SimpleExoPlayer mExoPlayer;
    public mySessionCallback(SimpleExoPlayer exoPlayer)
    {
        mExoPlayer=exoPlayer;
    }
    @Override
    public void onPlay() {
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onPause() {
        mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public void onSkipToPrevious() {
        mExoPlayer.seekTo(0);
    }
}
