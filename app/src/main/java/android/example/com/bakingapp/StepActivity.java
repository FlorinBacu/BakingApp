package android.example.com.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.example.com.bakingapp.Concepts.DataLoader;
import android.example.com.bakingapp.Concepts.Step;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import timber.log.Timber;

public class StepActivity extends AppCompatActivity {
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private TextView descView;
    private Button nextButton;
    private long seekPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Timber.d("I got the step description:");
        Timber.d(intent.getStringExtra("desc"));
        descView=findViewById(R.id.desc_step);
        descView.setText(intent.getStringExtra("desc"));
        simpleExoPlayerView= (SimpleExoPlayerView)findViewById(R.id.video_step);
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "BakingApp"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();



        descView = (TextView) findViewById(R.id.desc_step);

        nextButton=(Button)findViewById(R.id.next_step_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepActivityFragment.currentStepIndex= (StepActivityFragment.currentStepIndex+1)% DataLoader.ITEMS.get(RecipeDetailFragment.currentRecipeIndex).steps.size();
                Bundle arguments = new Bundle();
                Step step = DataLoader.ITEMS.get(RecipeDetailFragment.currentRecipeIndex).steps.get(StepActivityFragment.currentStepIndex);
                Context context = v.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("videoURL",step.videoUrl);
                intent.putExtra("desc",step.description);
                intent.putExtra("sent",true);
               startActivity(intent);




            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        outState.putLong("playerPos",player.getCurrentPosition()%player.getDuration());
        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if(savedInstanceState!=null)
            seekPosition=savedInstanceState.getLong("playerPos",0);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initializePlayer() {


        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(getIntent().getStringExtra("videoURL")),
                mediaDataSourceFactory, extractorsFactory, null, null);

        player.prepare(mediaSource);
        player.seekTo(seekPosition);

    }

    private void releasePlayer() {
        if (player != null) {
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
