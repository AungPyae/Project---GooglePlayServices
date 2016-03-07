package xyz.aungpyaephyo.gps.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

import xyz.aungpyaephyo.gps.R;
import xyz.aungpyaephyo.gps.fragments.ActivityRecognitionFragment;
import xyz.aungpyaephyo.gps.fragments.LocationFragment;

public class MainActivity extends BaseActivity
        implements LocationFragment.ControllerLocation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_main_container, new LocationFragment(), LocationFragment.TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTapActivityRecognition() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_main_container, new ActivityRecognitionFragment(), ActivityRecognitionFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onLocationRetrieved(Location location) {
        super.onLocationRetrieved(location);
        LocationFragment locationFragment = (LocationFragment) getSupportFragmentManager().findFragmentByTag(LocationFragment.TAG);
        if (locationFragment != null) {
            locationFragment.setLocation(location);
        } else {
            Toast.makeText(getApplicationContext(), "Can't find location fragment by tag", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivitiesDetected(ArrayList<DetectedActivity> detectedActivities) {
        super.onActivitiesDetected(detectedActivities);
        ActivityRecognitionFragment activityRecognitionFragment =
                (ActivityRecognitionFragment) getSupportFragmentManager().findFragmentByTag(ActivityRecognitionFragment.TAG);

        if (activityRecognitionFragment != null) {
            activityRecognitionFragment.setDetectedActivities(detectedActivities);
        } else {
            Toast.makeText(getApplicationContext(), "Can't find activity recognition fragment by tag", Toast.LENGTH_SHORT).show();
        }
    }
}
