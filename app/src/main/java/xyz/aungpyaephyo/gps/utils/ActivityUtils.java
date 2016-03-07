package xyz.aungpyaephyo.gps.utils;

import android.content.res.Resources;

import com.google.android.gms.location.DetectedActivity;

import xyz.aungpyaephyo.gps.GPSApplication;
import xyz.aungpyaephyo.gps.R;

/**
 * Created by aung on 3/7/16.
 */
public class ActivityUtils {

    public static String getActivityByType(int activityType) {
        Resources resources = GPSApplication.getContext().getResources();
        switch (activityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.UNKNOWN:
                return resources.getString(R.string.unknown);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            default:
                return resources.getString(R.string.unidentifiable_activity);
        }
     }
}
