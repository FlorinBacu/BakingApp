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
import android.widget.ImageView;
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
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepActivityFragment extends Fragment{

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    private TextView descView;
    private String descText;
    private String videoURL;
    private Button nextButton;
    boolean isPlaying=false;
    public static int currentStepIndex;
    public static long positionSeek=0;

    public StepActivityFragment() {
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isPlaying",true);
        outState.putLong("pos",positionSeek);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null) {
            isPlaying = savedInstanceState.getBoolean("isPlaying", false);
            positionSeek=savedInstanceState.getLong("pos",0);

        }
        else
        {
            positionSeek=0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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



            simpleExoPlayerView= (SimpleExoPlayerView)inflated.findViewById(R.id.video_step);
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this.getActivity(), Util.getUserAgent(this.getActivity(), "BakingApp"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();



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
                                    .replace(R.id.fragment_step,stepActivityFragment)
                                    .commit();


                    }
                });


        }


        return inflated;

    }
    private void initializePlayer() {

        simpleExoPlayerView =(SimpleExoPlayerView)getActivity().findViewById(R.id.video_step);
        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this.getActivity(), trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);


    DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

    MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
            mediaDataSourceFactory, extractorsFactory, null, null);

    player.prepare(mediaSource);
        player.seekTo(positionSeek);
}

    private void releasePlayer() {
        if (player != null) {
            positionSeek=player.getCurrentPosition()%player.getDuration();
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
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

}
