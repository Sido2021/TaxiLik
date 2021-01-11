package com.taxilik.client.home;

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
import com.taxilik.client.home.history.ClientHistoryFragment;
import com.taxilik.client.home.map.ClientMapFragment;
import com.taxilik.client.home.offre.ClientOffreFragment;

public class ClientHomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    public ClientHomeFragment() {
        // Required empty public constructor
    }

    public static ClientHomeFragment newInstance(String param1, String param2) {
        ClientHomeFragment fragment = new ClientHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_home, container, false);
    }



    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        Fragment childFragment = new ClientOffreFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment).commit();
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment childFragment = new ClientOffreFragment();
                switch (item.getItemId()){
                    case R.id.navigation_offre:
                         childFragment = new ClientOffreFragment();
                        break;

                    case R.id.navigation_history:
                        childFragment = new ClientHistoryFragment();
                        break;
                    case R.id.navigation_map:
                        childFragment = new ClientMapFragment();
                        break;
                }
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.child_fragment_container, childFragment).commit();
                BottomNavigationView bottomNavigationView = view.findViewById(R.id.nav_view);
                return true;
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromParentFragment(Uri uri);
    }
}