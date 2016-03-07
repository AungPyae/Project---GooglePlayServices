package xyz.aungpyaephyo.gps.activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import xyz.aungpyaephyo.gps.GPSApplication;
import xyz.aungpyaephyo.gps.controllers.ControllerActivityRecognition;
import xyz.aungpyaephyo.gps.intentservices.DetectedActivitiesIntentService;
import xyz.aungpyaephyo.gps.utils.GPSConstants;

/**
 * Created by aung on 3/7/16.
 */
public class BaseActivity extends AppCompatActivity implements
        ControllerActivityRecognition,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<Status> {

    private static final int ACCESS_PERMISSIONS_LOCATION_REQUEST = 100;
    private static final int ACCESS_PERMISSIONS_LAST_LOCATION = 101;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ActivityDetectionBroadcastReceiver mActivityDetectionBR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityDetectionBR = new ActivityDetectionBroadcastReceiver();
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mActivityDetectionBR,
                new IntentFilter(GPSConstants.BA_ACTIVITIES_DETECTED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mActivityDetectionBR);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACCESS_PERMISSIONS_LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    makeLocationRequest();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case ACCESS_PERMISSIONS_LAST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Location location = getLastLocation();
                    onLocationRetrieved(location);
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(GPSApplication.TAG, "GPS onConnected");

        /* Make location request with specified Priority & Frequency.
        makeLocationRequest();
        */

        Location location = getLastLocation();
        if (location != null) {
            onLocationRetrieved(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(GPSApplication.TAG, "GPS onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(GPSApplication.TAG, "GPS onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(GPSApplication.TAG, "GPS onLocationChanged");
        onLocationRetrieved(location);
    }

    /**
     * Build GoogleApiClient with required callback listeners.
     */
    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    /**
     * Make location request with Runtime permission checking.
     */
    private void makeLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(GPSConstants.LOCATION_DETECTION_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_PERMISSIONS_LOCATION_REQUEST);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Get Last Location with Runtime Permission checking.
     *
     * @return
     */
    private Location getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_PERMISSIONS_LAST_LOCATION);

            return null;
        }
        return LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Placeholder method for manipulating retrieved location object.
     * Not abstract because not every child activity would need Location.
     *
     * @param location
     */
    protected void onLocationRetrieved(Location location) {

    }

    /**
     * Placeholder method for manipulating detected activities.
     *
     * @param detectedActivities
     */
    protected void onActivitiesDetected(ArrayList<DetectedActivity> detectedActivities) {

    }

    @Override
    public void requestActivityUpdate() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Google Api Client is not connected", Toast.LENGTH_SHORT).show();
        } else {
            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                    mGoogleApiClient, GPSConstants.ACTIVITY_DETECTION_INTERVAL,
                    getActivityDetectionPendingIntent()).setResultCallback(this);
        }
    }

    @Override
    public void removeActivityUpdate() {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Google Api Client is not connected", Toast.LENGTH_SHORT).show();
        } else {
            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                    mGoogleApiClient,
                    getActivityDetectionPendingIntent()).setResultCallback(this);
        }
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.d(GPSApplication.TAG, "Successfully added / removed activity detection");
        } else {
            Log.d(GPSApplication.TAG, "Error adding / removing activity detection");
        }
    }

    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<DetectedActivity> detectedActivities = intent.getParcelableArrayListExtra(GPSConstants.IE_DETECTED_ACTIVITIES);
            if (detectedActivities != null) {
                onActivitiesDetected(detectedActivities);
            }
        }
    }
}
