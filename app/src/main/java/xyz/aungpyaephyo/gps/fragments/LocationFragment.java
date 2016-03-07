package xyz.aungpyaephyo.gps.fragments;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.aungpyaephyo.gps.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationFragment extends Fragment {

    public static final String TAG = LocationFragment.class.getSimpleName();

    private ControllerLocation mLocationController;

    @Bind(R.id.tv_location)
    TextView tvLocation;

    private Location mLocation;

    public LocationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLocationController = (ControllerLocation) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mLocation != null) {
            setLocation(mLocation);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocation = null;
    }

    public void setLocation(Location location) {
        this.mLocation = location;

        if(tvLocation != null)
            tvLocation.setText(location.getLatitude()+", "+location.getLongitude());
    }

    @OnClick(R.id.btn_to_activity_recognition)
    public void onTapActivityRecognition(View view) {
        mLocationController.onTapActivityRecognition();
    }

    public interface ControllerLocation {
        void onTapActivityRecognition();
    }
}
