package android.example.com.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import timber.log.Timber;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Timber.d("I got the step description:");
        Timber.d(intent.getStringExtra("desc"));
        Bundle arguments = new Bundle();
        arguments.putString("videoURL",intent.getStringExtra("videoURL"));
        arguments.putString("desc",intent.getStringExtra("desc"));
        arguments.putBoolean("sent",true);
        StepActivityFragment stepActivityFragment=new StepActivityFragment();
        stepActivityFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_step,stepActivityFragment)
                .commit();


    }

}
