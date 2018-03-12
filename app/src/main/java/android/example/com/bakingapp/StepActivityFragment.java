package android.example.com.bakingapp;

import android.example.com.bakingapp.Concepts.DataLoader;
import android.example.com.bakingapp.Concepts.MySessionCallback;
import android.example.com.bakingapp.Concepts.Step;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepActivityFragment extends Fragment implements ExoPlayer.EventListener {

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    boolean received=false;

    private TextView descView;
    private String descText;
    private String videoURL;
    private Button nextButton;

    public static int currentStepIndex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args!=null && args.getBoolean("sent")) {
            descText=args.getString("desc");
            videoURL=args.getString("videoURL");
            Timber.d("Step description is ");
            Timber.d(descText);
            /*currentRecipe=args.getInt("currentRecipeIndex");
            currentStep=args.getInt("currentStepIndex");*/


        }


    }

    public StepActivityFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null)
          mExoPlayer.seekTo(savedInstanceState.getLong("posPlayer",0));

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putLong("posPlayer",mExoPlayer.getCurrentPosition());
    }

    private void releasePlayer() {

        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroy() {

            super.onDestroy();
            mMediaSession.setActive(false);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_step, container, false);


                


                mPlayerView = (SimpleExoPlayerView)inflated.findViewById(R.id.video_step);
                descView = (TextView) inflated.findViewById(R.id.desc_step);
                nextButton=(Button)inflated.findViewById(R.id.next_step_button);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StepActivityFragment.currentStepIndex= (StepActivityFragment.currentStepIndex+1)% DataLoader.ITEMS.get(RecipeDetailFragment.currentRecipeIndex).steps.size();
                        Bundle arguments = new Bundle();
                        Step step = DataLoader.ITEMS.get(RecipeDetailFragment.currentRecipeIndex).steps.get(StepActivityFragment.currentStepIndex);

                        arguments.putString("videoURL",step.videoUrl);
                        arguments.putString("desc",step.description);
                        arguments.putBoolean("sent",true);
                        StepActivityFragment stepActivityFragment=new StepActivityFragment();
                        stepActivityFragment.setArguments(arguments);
                        if(RecipeListActivity.mTwoPane)
                        {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.step_detail_container,stepActivityFragment)
                                    .commit();
                        }
                        else
                        {
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_step,stepActivityFragment)
                                    .commit();
                        }

                    }
                });
                initializeMediaSession();


                mPlayerView.setPlayer(mExoPlayer);
                descView.setText(descText);
                initializePlayer(Uri.parse(videoURL));




        return inflated;

    }
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this.getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this.getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this.getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this.getActivity(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback(mExoPlayer));

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
