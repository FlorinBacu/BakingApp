package android.example.com.bakingapp;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
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
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import timber.log.Timber;

public class StepActivity extends AppCompatActivity {

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
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Timber.d("I got the step description:");
        Timber.d(intent.getStringExtra("desc"));

context=this;
        if(intent.getBooleanExtra("sent",false)) {
            descText=intent.getStringExtra("desc");
            videoURL=intent.getStringExtra("videoURL");
            Timber.d("Step description is ");
            Timber.d(descText);
            /*currentRecipe=args.getInt("currentRecipeIndex");
            currentStep=args.getInt("currentStepIndex");*/


            playerView = findViewById(R.id.video_step);




            descView = (TextView) findViewById(R.id.desc_step);
            descView.setText(descText);
            nextButton=(Button)findViewById(R.id.next_step_button);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StepActivityFragment.currentStepIndex= (StepActivityFragment.currentStepIndex+1)% DataLoader.ITEMS.get(RecipeDetailFragment.currentRecipeIndex).steps.size();
                    Bundle arguments = new Bundle();
                    Step step = DataLoader.ITEMS.get(RecipeDetailFragment.currentRecipeIndex).steps.get(StepActivityFragment.currentStepIndex);
                    Intent intent1=new Intent(context,StepActivity.class);
                    intent1.putExtra("videoURL",step.videoUrl);
                    intent1.putExtra("desc",step.description);
                    intent1.putExtra("sent",true);
                    startActivity(intent1);


                }
            });

            Log.i("TAG","restore");
            if (savedInstanceState != null) {
                Log.i("TAG","restore inside");
                currentWindow = savedInstanceState.getInt("winIndex");
                playbackPosition = savedInstanceState.getLong("position",0);

initializePlayer();

            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,RecipeListActivity.class);
        startActivity(intent);
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
        hideSystemUi();
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
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
        }
        MediaSource mediaSource = buildMediaSource(Uri.parse(videoURL));
        player.prepare(mediaSource, false, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("TAG","save");
        super.onSaveInstanceState(outState);

        outState.putInt("winIndex", currentWindowState);
        outState.putLong("position", playbackPositionState);

    }



    private void releasePlayer() {
        if (player != null) {

            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playbackPositionState=playbackPosition;
            currentWindowState=currentWindow;
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



