package com.taxilik.client.home.history;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.taxilik.Data.CurrentUser;

public class ClientHistoryFragment extends Fragment {


    ProgressDialog pdDialog;

    CheckBox [] boxItem=new CheckBox[3];
    CheckBox princip;

    String URL_HISTORY = "https://omega-store.000webhostapp.com/getUserHistories.php";

    ListView listViewClientHistories ;
    ArrayList<History> listHistories = new ArrayList<>() ;
    public ClientHistoryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_client_history, container, false);


        listViewClientHistories = v.findViewById(R.id.list_client_histories);

        pdDialog= new ProgressDialog(getContext());
        pdDialog.setTitle("Login please wait...");
        pdDialog.setCancelable(false);

        princip=(CheckBox) v.findViewById(R.id.checkBox);
        /*for (int i = 0; i < boxItem.length; i++) { boxItem[i] = (CheckBox) v.findViewById(getIdByName("check_" + (i + 1))); }

        princip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getContext(), "Elemnts va supprimer", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < boxItem.length; i++) {boxItem[i].setVisibility(View.VISIBLE); boxItem[i].setChecked(true); }
                }else{

                    for (int i = 0; i < boxItem.length; i++) {
                        boxItem[i].setChecked(false); }

                }

            }
        });*/

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getClientHistories();
    }

    /*public static int getIdByName(final String name) {

        try {

            final Field field = R.id.class.getDeclaredField(name);

            field.setAccessible(true);
            return field.getInt(null);

        } catch (Exception ignore) {
            return -1;
        }
    }*/

    private void getClientHistories(){
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
                                    int drivreId = history.getInt("driver_id");
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

                                    User driver = new User(drivreId,firstName,lastName,phone,image,2,"");
                                    Car car = new Car(carId,"","","");

                                    History h = new History(requestId,car,dateStart,dateEnd,driver,null);
                                    listHistories.add(h);
                                }
                                ClientHistoryAdapter adapter = new ClientHistoryAdapter(getContext(),listHistories);
                                listViewClientHistories.setAdapter(adapter);
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
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",CurrentUser.getId()+"");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}