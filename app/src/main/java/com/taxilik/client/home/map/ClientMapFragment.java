package com.taxilik.client.home.map;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.taxilik.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ClientMapFragment extends Fragment {

    GoogleMap map ;
    Location currentLocation ;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static final int REQUEST_CODE = 101;
    String URL_GET_NEAR_CARS = "https://omega-store.000webhostapp.com/get_near_cars.php";
    ProgressDialog pdDialog;


    ArrayList<MarkerOptions> carsList = new ArrayList<>();

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            createTestLocation();
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.map_style));
        }
    };

    private void createTestLocation() {

        LatLng latLng = new LatLng(34.402108422360705,-2.8970004531777307);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Test Location");

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        map.addMarker(markerOptions);

        map.addCircle(new CircleOptions()
                .center(new LatLng(latLng.latitude, latLng.longitude))
                .radius(1000).strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(0x3f0000ff));

        loadNearCars();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            pdDialog= new ProgressDialog(getContext());
            pdDialog.setTitle("wait...");
            pdDialog.setCancelable(false);

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            mapFragment.getMapAsync(callback);
        }


    }
    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        final Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location !=null){

                    currentLocation = location ;

                    LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Location");

                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    map.addMarker(markerOptions);

                    Circle circle = map.addCircle(new CircleOptions()
                            .center(new LatLng(latLng.latitude, latLng.longitude))
                            .radius(1000).strokeWidth(2)
                            .strokeColor(Color.BLUE)
                            .fillColor(0x3f0000ff));


                }
                else {
                    Toast.makeText(getContext(), task.getException()+"", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void loadNearCars()
    {
        pdDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_NEAR_CARS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray carsArray = jsonObject.getJSONArray("cars");

                            if(success.equals("0")){
                                for(int i=0;i<carsArray.length();i++){
                                    JSONObject car = carsArray.getJSONObject(i);
                                    //Toast.makeText(getContext(),car.getString("car_id") + " , "+ car.getString("distance"),Toast.LENGTH_LONG).show();

                                    LatLng latLng = new LatLng(Double.parseDouble(car.getString("latitude")),Double.parseDouble(car.getString("longitude")));
                                    MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("car :"+car.getString("car_id"));

                                    if(Double.parseDouble(car.getString("distance")) <1){
                                        markerOptions.icon(bitmapDescriptorFromVector(getContext(),R.drawable.ic_car_gold));
                                    }
                                    else {
                                        markerOptions.icon(bitmapDescriptorFromVector(getContext(),R.drawable.ic_car_gray));
                                    }

                                    carsList.add(markerOptions);
                                    map.addMarker(markerOptions);
                                }

                                //startListining();

                            }
                            pdDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            pdDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
                Toast.makeText(getContext(),"Registration Error !2"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                //test location 34.402108422360705, -2.8970004531777307
                params.put("city","Taourirt");
                params.put("latitude","34.402108422360705");
                params.put("longitude","-2.8970004531777307");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //fetchLastLocation();
                }
            }
        }
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}