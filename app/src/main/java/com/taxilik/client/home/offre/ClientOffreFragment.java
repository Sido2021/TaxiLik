package com.taxilik.client.home.offre;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.taxilik.Car;
import com.taxilik.Data;
import com.taxilik.R;
import com.taxilik.User;
import com.taxilik.client.home.ClientHomeFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientOffreFragment extends Fragment {

    ProgressDialog pdDialog;

    String URL_GET_NEAR_CARS = "https://omega-store.000webhostapp.com/get_near_cars.php";

    ListView listTaxi;
    Button btnVip,btnTaxi;
    View dividerVip,dividerTaxi;
    FirebaseFirestore db ;

    ArrayList<Long> ordredCarsID =  new ArrayList<>();

    public ClientOffreFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_client_offre, container, false);

        db = FirebaseFirestore.getInstance();

        pdDialog= new ProgressDialog(getContext());
        pdDialog.setTitle("please wait...");
        pdDialog.setCancelable(false);

        listTaxi=v.findViewById(R.id.listViewTaxi);
        // buttion vip
        btnVip=v.findViewById(R.id.button);dividerVip=v.findViewById(R.id.divider4);
        btnTaxi=v.findViewById(R.id.button2);dividerTaxi=v.findViewById(R.id.divider);
        btnVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnVip.setTextColor(Color.parseColor("#fb9403"));dividerVip.setBackgroundColor(Color.parseColor("#fb9403"));
                btnTaxi.setTextColor(Color.parseColor("#000000"));dividerTaxi.setBackgroundColor(Color.parseColor("#EAEAEA"));
                Toast.makeText(getContext(), "Aucun Vip voitures", Toast.LENGTH_SHORT).show();
            }
        });

        btnTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnVip.setTextColor(Color.parseColor("#000000"));dividerVip.setBackgroundColor(Color.parseColor("#EAEAEA"));
                btnTaxi.setTextColor(Color.parseColor("#fb9403")); dividerTaxi.setBackgroundColor(Color.parseColor("#fb9403"));

            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        loadOrdredCars();
    }

    private void loadNearCars()
    {
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
                                ArrayList<Car> carsList = new ArrayList<>();
                                for (int i =0;i<carsArray.length();i++){
                                    JSONObject car = carsArray.getJSONObject(i);
                                    if(Double.parseDouble(car.getString("distance")) <1) {

                                        int userId = car.getInt("driver_id");
                                        String firstName = car.getString("first_name");
                                        String lastName = car.getString("last_name");
                                        String phone = car.getString("phone");
                                        String image = car.getString("driver_image");
                                        int type = car.getInt("type");

                                        User driver = new User(userId,firstName,lastName,phone,image,type,"");

                                        int carId = car.getInt("car_id");
                                        String carImage = car.getString("car_image");
                                        String carMatricule = car.getString("matricule");
                                        double latitude = car.getDouble("latitude");
                                        double longitude = car.getDouble("longitude");

                                        Car c = new Car(carId,latitude,longitude, carImage,driver,carMatricule );
                                        c.setOrdred(ordredCarsID.contains((long)Integer.parseInt(car.getString("car_id"))));
                                        carsList.add(c);
                                    }
                                }
                                OffreAdapter adapter=new OffreAdapter(getContext(),carsList,getParentFragment());
                                listTaxi.setAdapter(adapter);
                                pdDialog.dismiss();
                            }
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
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        void messageFromChildFragment(Uri uri);
    }

    private void loadOrdredCars() {
        pdDialog.show();
        db.collection("OnlineOrder").whereEqualTo("ClientID", Data.CurrentUser.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Long cd = document.getLong("CarID");
                        ordredCarsID.add(cd);
                        Toast.makeText(getContext(), "CarID"+cd, Toast.LENGTH_SHORT).show();
                    }
                    loadNearCars();
                } else {
                    pdDialog.dismiss();
                }
            }
        });
    }

}