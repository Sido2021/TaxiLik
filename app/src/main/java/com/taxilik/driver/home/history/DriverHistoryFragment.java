package com.taxilik.driver.home.history;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.taxilik.Car;
import com.taxilik.History;
import com.taxilik.R;
import com.taxilik.User;
import com.taxilik.client.home.history.ClientHistoryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.taxilik.Data.CurrentUser;


public class DriverHistoryFragment extends Fragment {

    String URL_HISTORY = "https://omega-store.000webhostapp.com/getDriverHistories.php";

    ProgressDialog pdDialog;
    ListView listViewDriverHistories ;
    ArrayList<History> listHistories = new ArrayList<>() ;

    public DriverHistoryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_driver_history, container, false);


        listViewDriverHistories = v.findViewById(R.id.list_driver_histories);

        pdDialog= new ProgressDialog(getContext());
        pdDialog.setTitle("¨Please Wait...");
        pdDialog.setCancelable(false);

        return v;
    }

    public interface OnFragmentInteractionListener {
        void messageFromParentFragment(Uri uri);
    }

    private void getDriverHistories(){
        pdDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray histories = jsonObject.getJSONArray("histories");

                            if(success.equals("0")){
                                for (int i=0;i<histories.length();i++){
                                    JSONObject history = histories.getJSONObject(i);

                                    int requestId = history.getInt("request_id");

                                    int carId = history.getInt("car_id");
                                    String matricule = history.getString("matricule");
                                    String carImage = history.getString("car_image");
                                    double latitude = history.getDouble("latitude");
                                    double longitude = history.getDouble("longitude");

                                    int clientId = history.getInt("user_id");
                                    String firstName = history.getString("first_name");
                                    String lastName = history.getString("last_name");
                                    String phone = history.getString("phone");
                                    String image = history.getString("image");

                                    Date dateStart = null;
                                    Date dateEnd = null;

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                                    try {
                                        dateStart = simpleDateFormat.parse(history.getString("date_start"));
                                        dateEnd = simpleDateFormat.parse(history.getString("date_end"));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    User client = new User(clientId,firstName,lastName,phone,image,1,"");
                                    Car car = new Car(carId,latitude,longitude,carImage,null,matricule);

                                    History h = new History(requestId,car,dateStart,dateEnd,null,client);
                                    listHistories.add(h);
                                }
                                ClientHistoryAdapter adapter = new ClientHistoryAdapter(getContext(),listHistories);
                                listViewDriverHistories.setAdapter(adapter);
                            }
                            pdDialog.dismiss();
                        } catch (Exception e) {
                            Log.e("error",e.getMessage());
                            e.printStackTrace();
                            pdDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("driver_id",CurrentUser.getId()+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}