package com.taxilik.client.home.offre;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxilik.CarsDipo;
import com.taxilik.R;
import com.taxilik.TaxiAdapter;
import com.taxilik.client.home.ClientHomeFragment;
import com.taxilik.indexUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientOffreFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressDialog pdDialog;
    String URL_GET_NEAR_CARS = "https://omega-store.000webhostapp.com/get_near_cars.php";

    ListView listTaxi;

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


        pdDialog= new ProgressDialog(getContext());
        pdDialog.setTitle("Login please wait...");
        pdDialog.setCancelable(false);

        //listView
        listTaxi=v.findViewById(R.id.listViewTaxi);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadNearCars();
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
                                ArrayList<CarsDipo> carsList = new ArrayList<>();
                                for(int i=0;i<carsArray.length();i++){
                                    JSONObject car = carsArray.getJSONObject(i);
                                    //Toast.makeText(getContext(),car.getString("car_id") + " , "+ car.getString("distance"),Toast.LENGTH_LONG).show();

                                    if(Double.parseDouble(car.getString("distance")) <1){
                                        CarsDipo carsDipo = new CarsDipo("",car.getString("driver_name"),"");
                                        carsList.add(carsDipo);
                                    }

                                }
                                TaxiAdapter adapter=new TaxiAdapter(getContext(),R.layout.car_list,carsList);
                                listTaxi.setAdapter(adapter);
                            }
                            pdDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Registration Error !1"+e,Toast.LENGTH_LONG).show();
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