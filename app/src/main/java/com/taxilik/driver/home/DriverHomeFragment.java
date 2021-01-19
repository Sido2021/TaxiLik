package com.taxilik.driver.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.taxilik.R;
import com.taxilik.client.home.ClientHomeFragment;
import com.taxilik.driver.home.history.DriverHistoryFragment;
import com.taxilik.driver.home.map.DriverMapFragment;
import com.taxilik.driver.home.request.DriverRequestFragment;

import org.jetbrains.annotations.NotNull;

public class DriverHomeFragment extends Fragment {


    public DriverHomeFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        Fragment childFragment = new DriverRequestFragment();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment childFragment = new DriverRequestFragment();
                switch (item.getItemId()){
                    case R.id.navigation_offre:
                        childFragment = new DriverRequestFragment();
                        break;

                    case R.id.navigation_history:
                        childFragment = new DriverMapFragment();
                        break;
                    case R.id.navigation_map:
                        childFragment = new DriverHistoryFragment();
                        break;
                }
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.child_fragment_container, childFragment).commit();
                return true;
            }
        });
    }
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void messageFromParentFragment(Uri uri);
    }
}