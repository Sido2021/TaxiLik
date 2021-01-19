package com.taxilik.driver.home.history;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.taxilik.R;


public class DriverHistoryFragment extends Fragment {

    public DriverHistoryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_driver_history, container, false);
    }

    public interface OnFragmentInteractionListener {
        void messageFromParentFragment(Uri uri);
    }

}