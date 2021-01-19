package com.taxilik.client.home.offre;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxilik.R;


public class ClientOffreFragmentClick extends Fragment {

    GoogleMap map ;
    Location currentLocation ;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    TextView drivername,matVoiture;
    String chauffeur,mat;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            createTestLocation();
        }
    };
    private void createTestLocation() {
        /*currentLocation = new Location("");
        currentLocation.setLatitude(34.402108422360705);
        currentLocation.setLongitude(-2.8970004531777307);*/

        LatLng latLng = new LatLng(34.402108422360705,-2.8970004531777307);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Test Location");

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        map.addMarker(markerOptions);

        map.addCircle(new CircleOptions()
                .center(new LatLng(latLng.latitude, latLng.longitude))
                .radius(1000).strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(0x3f0000ff));
    }

    public ClientOffreFragmentClick() {}

    public static ClientOffreFragmentClick newInstance(String param1, String param2) {
        ClientOffreFragmentClick fragment = new ClientOffreFragmentClick();
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
            //chaimae

           chauffeur = getArguments().getString("Chauffeurname");
           mat = getArguments().getString("matricule");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_client_offre_click,container,false);
      drivername = v.findViewById(R.id.NomPrenoCh);
        matVoiture = v.findViewById(R.id.matVoi);
        drivername.setText(chauffeur);
        matVoiture.setText(mat);

      //  return inflater.inflate(R.layout.fragment_client_offre_click, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MapView mapView = view.findViewById(R.id.mapView);
        mapView.getMapAsync(callback);
    }
}