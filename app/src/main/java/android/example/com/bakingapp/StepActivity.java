package android.example.com.bakingapp;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Timber.d("I got the step description:");
        Timber.d(intent.getStringExtra("desc"));
        StepActivityFragment stepActivityFragment=new StepActivityFragment();
        Bundle arg=new Bundle();
        arg.putString("videoURL",intent.getStringExtra("videoURL"));
        arg.putString("desc",intent.getStringExtra("desc"));
        arg.putBoolean("sent",true);
        StepActivityFragment stepActivityFragment1=new StepActivityFragment();
        stepActivityFragment.setArguments(arg);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_step,   stepActivityFragment)
                .commit();


    }


}



