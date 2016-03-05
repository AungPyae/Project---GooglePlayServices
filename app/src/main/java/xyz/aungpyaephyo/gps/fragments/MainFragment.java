package xyz.aungpyaephyo.gps.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.aungpyaephyo.gps.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    @Bind(R.id.tv_location)
    TextView tvLocation;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void setLocation(String location) {
        tvLocation.setText(location);
    }
}
