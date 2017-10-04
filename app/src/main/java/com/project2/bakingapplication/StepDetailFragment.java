package com.project2.bakingapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

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

import com.project2.bakingapplication.utilities.Step;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepDetailFragment.OnClickButtonHandler} interface
 * to handle interaction events.
 */
public class StepDetailFragment extends Fragment {

    private static String TAG = StepDetailFragment.class.getSimpleName();
    private OnClickButtonHandler onClickButtonHandler;
    private TextView mTextNoVideo;
    private TextView mStepInstructions;
    private Uri mVideoURI;
    private SimpleExoPlayerView mStepVideoView;
    private String mCurrentStepIndex;
    private Button mPreviousButton;
    private Button mNextButton;
    private Step mCurrentStep;
    private SimpleExoPlayer mExoPlayer;
   // private SimpleExoPlayerView mPlayerView;
    //private static MediaSessionCompat mMediaSession;



    public interface OnClickButtonHandler {
        // TODO: Update argument type and name
        Step onClickPrevious(Step previousStep);
        Step onClickNext(Step nextStep);
    }


    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentRootView = inflater.inflate(R.layout.fragment_video_step,container,false);
        mTextNoVideo = (TextView) fragmentRootView.findViewById(R.id.text_no_video);
        // Initialize the exoplayer view.
        mStepVideoView = (SimpleExoPlayerView) fragmentRootView.findViewById(R.id.recipe_step_video);
        mStepInstructions = (TextView) fragmentRootView.findViewById(R.id.recipe_step_instructions);
        // initialize the buttons but in Tablet device, we do not have the buttons

        View detail_land = (View)fragmentRootView.findViewById(R.id.fragment_recipe_detail_land);

        // In portrait mode - initialize the Previous and Next buttons
        if(detail_land == null) {

            mPreviousButton = fragmentRootView.findViewById(R.id.button_previous);
            mNextButton = fragmentRootView.findViewById(R.id.button_next);

            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: load the previou step data
                    Step previousStep = onClickButtonHandler.onClickPrevious(mCurrentStep);
                    if (previousStep != null) {
                        // TODO: check the step
                        setStepData(previousStep);
                    } else {

                        Toast.makeText(getContext(), "No more previous step!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: load the next step
                    Step nextStep = onClickButtonHandler.onClickNext(mCurrentStep);
                    if (nextStep != null) {
                        setStepData(nextStep);
                    } else {
                        Toast.makeText(getContext(), "No more Next step!", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }


        return fragmentRootView;
    }

    /**
     * setStepData
     * @param step
     */
    public void setStepData(Step step){
        mCurrentStep = step;
       // mCurrentStepIndex = step.getStepId();
        mStepInstructions.setText(step.getDescription());
        String videoURL = step.getVideoURL();
        if(videoURL != null && !videoURL.isEmpty()) {

            //String dummyURI = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdb3a_3-mix-sugar-salt-together-cheesecake/3-mix-sugar-salt-together-cheesecake.mp4";
            //mVideoURI =  Uri.parse(dummyURI);
            mVideoURI =  Uri.parse(videoURL);
            Log.d(TAG, "Video URI is:" + mVideoURI.toString());
            // TODO: check how to use Media play to play the movie
            mTextNoVideo.setVisibility(View.INVISIBLE);
            mStepVideoView.setVisibility(View.VISIBLE);

            // Initialize the player.
            initializePlayer(mVideoURI);

        } else {
            // No video data
            mTextNoVideo.setVisibility(View.VISIBLE);
            mStepVideoView.setVisibility(View.INVISIBLE);
        }

    }


    public void disablePreviousButton(boolean disable){
        if(disable) {
            mPreviousButton.setVisibility(View.INVISIBLE);
        } else {
            mPreviousButton.setVisibility(View.VISIBLE);
        }

    }

    public void disableNextButton(boolean disable) {

        if(disable) {
            mNextButton.setVisibility(View.INVISIBLE);
        } else {
            mNextButton.setVisibility(View.VISIBLE);
        }

    }



    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // TODO set OnClickListener for the Previous and Next button
        if (context instanceof StepDetailFragment.OnClickButtonHandler) {
            onClickButtonHandler = (OnClickButtonHandler) context;
        } else {

            throw new RuntimeException(context.toString()
                   + " must implement OnClickButtonHandler");
        }

//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onClickButtonHandler = null;
    }



    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();

    }

    /**
     * Initialize ExoPlayer.
     * @param uri The URI of the sample to play.
     */
    private void initializePlayer(Uri uri) {
        String userAgent = Util.getUserAgent(getContext(), StepDetailFragment.class.getName());
        if (mExoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mStepVideoView.setPlayer(mExoPlayer);
            // Prepare the MediaSource the very first time after ExoPlayer is initialized
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        } else {
            // Prepare the MediaSource after Exoplayer is already initialized
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        //mNotificationManager.cancelAll();
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }



}
