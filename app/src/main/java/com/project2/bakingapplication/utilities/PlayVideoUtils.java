package com.project2.bakingapplication.utilities;


import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.project2.bakingapplication.StepDetailFragment;

// exoplayer classes
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class PlayVideoUtils {

    /**
     * Release ExoPlayer.
     */
    public static void releasePlayer(SimpleExoPlayer exoPlayer) {
        //mNotificationManager.cancelAll();
        if(exoPlayer!=null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    public static SimpleExoPlayer initializePlayer(Uri uri, Context context, Class clazz) {
        SimpleExoPlayer mExoPlayer = null;
        String userAgent = Util.getUserAgent(context, clazz.getName());
       // if (mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
           // mStepVideoView.setPlayer(mExoPlayer);
            // Prepare the MediaSource the very first time after ExoPlayer is initialized
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

//        } else {
//            // Prepare the MediaSource after Exoplayer is already initialized
//            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
//                    context, userAgent), new DefaultExtractorsFactory(), null, null);
//            mExoPlayer.prepare(mediaSource);
//            mExoPlayer.setPlayWhenReady(true);
//
//        }
        return mExoPlayer;

    }

    public static void setMediaSourceOnly(Uri uri, Context context, Class clazz,SimpleExoPlayer exoPlayer) {
        String userAgent = Util.getUserAgent(context, clazz.getName());
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    context, userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

    }
}
