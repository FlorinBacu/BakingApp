package android.example.com.bakingapp;

import android.annotation.SuppressLint;
import android.example.com.bakingapp.Concepts.DataLoader;
import android.example.com.bakingapp.Concepts.MySessionCallback;
import android.example.com.bakingapp.Concepts.Step;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
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
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepActivityFragment extends Fragment{

    private SimpleExoPlayer player;
    private SimpleExoPlayerView playerView;

    private  long playbackPosition;
    private  int currentWindow;
    private boolean playWhenReady = true;
    private int currentWindowState;
    private long playbackPositionState;

    private TextView descView;
    private String descText;
    private String videoURL;
    private Button nextButton;

    public static int currentStepIndex;
    public static long positionSeek=0;

    public StepActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        playbackPosition= C.TIME_UNSET;
Timber.d("OncreateFragment");
        View inflated = inflater.inflate(R.layout.fragment_step, container, false);

        Bundle args = getArguments();
        if(args.getBoolean("sent")) {
            descText=args.getString("desc");
            videoURL=args.getString("videoURL");
            Timber.d("Step description is ");
            Timber.d(descText);
            /*currentRecipe=args.getInt("currentRecipeIndex");
            currentStep=args.getInt("currentStepIndex");*/


            playerView = inflated.findViewById(R.id.video_step);




                descView = (TextView) inflated.findViewById(R.id.desc_step);
                descView.setText(descText);
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


                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.step_detail_container,stepActivityFragment)
                                    .commit();


                    }
                });



        }


        return inflated;

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("TAG","restore");
        if (savedInstanceState != null) {
            Log.i("TAG","restore inside");
            currentWindow = savedInstanceState.getInt("winIndex");
            playbackPosition = savedInstanceState.getLong("position",C.TIME_UNSET);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("TAG","save");
        super.onSaveInstanceState(outState);

        outState.putInt("winIndex", currentWindow);
        outState.putLong("position", playbackPosition);

    }
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this.getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            MediaSource mediaSource = buildMediaSource(Uri.parse(videoURL));
            player.prepare(mediaSource, false, false);

            player.seekTo(currentWindow, playbackPosition);
            player.setPlayWhenReady(playWhenReady);
        }



    }



    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("bakingapp"))
                .createMediaSource(uri);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

}
