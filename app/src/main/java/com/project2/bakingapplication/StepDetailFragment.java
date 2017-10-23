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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

// exoplayer classes
import com.google.android.exoplayer2.C;
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
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepDetailFragment.OnClickButtonHandler} interface
 * to handle interaction events.
 */
public class StepDetailFragment extends Fragment {

    private static String TAG = StepDetailFragment.class.getSimpleName();
    private static String VIDEO_POSITION_KEY = "video_position";
    private static String CURRENT_STEP_KEY = "current_step";
    private static String CURRENT_WINDOW_POSITION_KEY = "current_window_position";
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
    private ImageView mThumbNailImage;
    private boolean isVideoPlaying;
    private long mVideoPosition = -1;
    private int mCurrentwindowIndex = -1;


   // Callback interface
    public interface OnClickButtonHandler {
        public Step onClickPrevious(Step previousStep);
        public Step onClickNext(Step nextStep);
    }


    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG,"Fragment - onCreateView");
        View fragmentRootView = inflater.inflate(R.layout.fragment_video_step,container,false);

        // add back the ThumbNail image View
        mThumbNailImage = (ImageView) fragmentRootView.findViewById(R.id.recipe_thumbNailImage);
        if ( mThumbNailImage == null ) {
            Log.d(TAG, "onCreate() -> mThumbNailImage is NULL !!! why ? !!");
        }

        mTextNoVideo = (TextView) fragmentRootView.findViewById(R.id.text_no_video);
        // Initialize the exoplayer view.
        mStepVideoView = (SimpleExoPlayerView) fragmentRootView.findViewById(R.id.recipe_step_video);
        mStepInstructions = (TextView) fragmentRootView.findViewById(R.id.recipe_step_instructions);

        // initialize the buttons but in Tablet device, we do not have the buttons

        View detail_land = (View)fragmentRootView.findViewById(R.id.fragment_recipe_detail_land);

        // TODO: Restore the saved instance state
        if (savedInstanceState!=null) {
           if (savedInstanceState.containsKey(CURRENT_STEP_KEY)) {
               mCurrentStep = savedInstanceState.getParcelable(CURRENT_STEP_KEY);
               Log.d(TAG,"CURRENT_STEP_KEY exist:" +  mCurrentStep.getStepId());
               setStepData(mCurrentStep);
           }
           if (savedInstanceState.containsKey(VIDEO_POSITION_KEY)) {
               mVideoPosition = savedInstanceState.getLong(VIDEO_POSITION_KEY);
           }
           if (savedInstanceState.containsKey(CURRENT_WINDOW_POSITION_KEY)) {
               mCurrentwindowIndex = savedInstanceState.getInt(CURRENT_WINDOW_POSITION_KEY);
           }
        } else {

            // reset position for exoPlayer's window index and position
            resetPosition();

        }

        // In portrait mode - initialize the Previous and Next buttons
        if(detail_land == null) {
            Log.d(TAG,"Portrait mode!");
            mPreviousButton = fragmentRootView.findViewById(R.id.button_previous);
            mNextButton = fragmentRootView.findViewById(R.id.button_next);

            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // load the previous step data by calling callback
                    Step previousStep = onClickButtonHandler.onClickPrevious(mCurrentStep);
                    if (previousStep != null) {
                        // set the step
                        setStepData(previousStep);
                    } else {

                        Toast.makeText(getContext(), "No more previous step!", Toast.LENGTH_LONG).show();
                    }
                }
            });

            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // load the next step by calling callback
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
     * setImage
     * @param step
     */
    public void setImage(Step step) {

        // TODO: add back the handle to display thumbnail image
        String thumbNailURLUrl= step.getThumbNailURL();
        if (thumbNailURLUrl!=null && !thumbNailURLUrl.isEmpty()) {
            Uri imageUri = Uri.parse(thumbNailURLUrl);
            Picasso.with(getContext()).load(imageUri).into(mThumbNailImage);
            mThumbNailImage.setVisibility(View.VISIBLE);
        } else {
            if ( mThumbNailImage == null ) {
                Log.e(TAG, "mThumbNailImage is NULL !!! why ? !!");
            }
            // mThumbNailImage.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * setStepData
     * @param step
     */
    public void setStepData(Step step) {
        mCurrentStep = step;
        Log.d(TAG, "Set Current Step ID:" + step.getStepId());
        Log.d(TAG, "Set Current Step Description:" + step.getDescription());
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
            Log.d(TAG, "Initialize the video");
            initializePlayer(mVideoURI);

        } else {
            // No video data
            mTextNoVideo.setVisibility(View.VISIBLE);
            mStepVideoView.setVisibility(View.INVISIBLE);
        }

        // Set step's thumbnail image
        setImage(step);

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // set OnClickListener for the Previous and Next button
        if (context instanceof StepDetailFragment.OnClickButtonHandler) {
            onClickButtonHandler = (OnClickButtonHandler) context;
        } else {

            throw new RuntimeException(context.toString()
                   + " must implement OnClickButtonHandler");
        }
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
        Log.d(TAG, "onDestroy - release Player");
        releasePlayer();

    }

    /**
     * Resubmit changes
     */
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart - initializePlayer");
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save the state of the player and release the resources
        if(mExoPlayer != null){
            //mExoPlayer.getContentPosition();
            Log.d(TAG, "onPause - save the video position and window index");
            mVideoPosition = mExoPlayer.getCurrentPosition();
            mCurrentwindowIndex = mExoPlayer.getCurrentWindowIndex();
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // resume the video
        Log.d(TAG, "onResume - initialize the player");
        String videoURL = mCurrentStep.getVideoURL();
        initializePlayer(Uri.parse(videoURL));

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


            boolean haveResumePosition = mCurrentwindowIndex != C.INDEX_UNSET;
            if (haveResumePosition) {
                mExoPlayer.seekTo(mCurrentwindowIndex, mVideoPosition);
            }
            mExoPlayer.prepare(mediaSource, !haveResumePosition, false);

            // if video position is saved
//            if (mVideoPosition != C.POSITION_UNSET) {
//                Log.d(TAG,"initialize player - set video position:" + String.valueOf(mVideoPosition));
//                mExoPlayer.seekTo(mVideoPosition);
//            }
//            if (mCurrentwindowIndex != C.INDEX_UNSET) {
//                Log.d(TAG,"initialize player - set window position:" + String.valueOf(mCurrentwindowIndex));
//               // mExoPlayer.seekToDefaultPosition(mCurrentwindowIndex);
//                mExoPlayer.seekTo(mCurrentwindowIndex, mVideoPosition);
//            }

            mExoPlayer.setPlayWhenReady(true);

        } else {
            // Prepare the MediaSource after Exoplayer is already initialized
            MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            boolean haveResumePosition = mCurrentwindowIndex != C.INDEX_UNSET;
            if (haveResumePosition) {
                mExoPlayer.seekTo(mCurrentwindowIndex, mVideoPosition);
            }
            mExoPlayer.prepare(mediaSource, !haveResumePosition, false);
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

    /**
     * Reset Video Position
     */
    private void resetPosition() {
        mCurrentwindowIndex = C.INDEX_UNSET;
        mVideoPosition = C.TIME_UNSET;
    }


    //TODO: Need to save the state of the video and the current step
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_STEP_KEY, mCurrentStep);
        outState.putLong(VIDEO_POSITION_KEY, mVideoPosition);
        outState.putInt(CURRENT_WINDOW_POSITION_KEY, mCurrentwindowIndex);
    }
}
