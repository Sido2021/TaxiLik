package com.taxilik.client.home.offre;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.taxilik.CarsDipo;
import com.taxilik.R;
import com.taxilik.TaxiAdapter;
import com.taxilik.client.home.ClientHomeFragment;
import com.taxilik.indexUser;

public class ClientOffreFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ClientHomeFragment.OnFragmentInteractionListener mListener;

    public ClientOffreFragment() {
        // Required empty public constructor
    }

    public static ClientOffreFragment newInstance(String param1, String param2) {
        ClientOffreFragment fragment = new ClientOffreFragment();
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
        View v=inflater.inflate(R.layout.fragment_client_offre, container, false);

        //listView
        ListView listTxi=(ListView)v.findViewById(R.id.listViewTaxi);
        CarsDipo cars1 =new CarsDipo("icon2","chaimae taj","mat:23565356");
        CarsDipo cars2 =new CarsDipo("icon2","youssef lianboui","mat:23556");
        CarsDipo cars3 =new CarsDipo("icon2","ahmad ahmad","mat:235454556");
        CarsDipo cars4 =new CarsDipo("icon2","ahmad trello","mat:23565356");
        CarsDipo cars5 =new CarsDipo("icon2","test test","mat:23565356");
        CarsDipo cars6 =new CarsDipo("icon2","ok ok","mat:23565356");
        CarsDipo[] listcars={cars1,cars2,cars3,cars4,cars5,cars6};
        //intialise wmlfile
        int xmlFile=R.layout.car_list;
        //call adapter
        TaxiAdapter adapter=new TaxiAdapter(v.getContext(),xmlFile,listcars);
        listTxi.setAdapter(adapter);

        // Inflate the layout for this fragment
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (ClientHomeFragment.OnFragmentInteractionListener) context;
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
        void messageFromChildFragment(Uri uri);
    }
}