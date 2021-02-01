package com.taxilik;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    EditText editTextSearch ;
    GridView gridViewSearch ;
    String URL_SEARCH = "https://omega-store.000webhostapp.com/search.php";
    ProgressDialog pdDialog;
    ImageButton imageButtonSeaView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editTextSearch = findViewById(R.id.edit_text_search);
        imageButtonSeaView = findViewById(R.id.image_button_search);
        gridViewSearch = findViewById(R.id.grid_view_search);

        pdDialog= new ProgressDialog(this);
        pdDialog.setTitle("Searching ...");
        pdDialog.setCancelable(false);

        imageButtonSeaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    private void search() {
        pdDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray carsArray = jsonObject.getJSONArray("cars");

                            if(success.equals("0")) {
                                ArrayList<Car> carsList = new ArrayList<>();
                                Toast.makeText(SearchActivity.this, "hi", Toast.LENGTH_SHORT).show();

                                for (int i = 0; i < carsArray.length(); i++) {
                                    JSONObject car = carsArray.getJSONObject(i);

                                    int userId = car.getInt("user_id");
                                    String firstName = car.getString("first_name");
                                    String lastName = car.getString("last_name");
                                    String phone = car.getString("phone");
                                    String image = car.getString("user_image");
                                    int type = car.getInt("type");

                                    User driver = new User(userId,firstName,lastName,phone,image,type,"");

                                    int carId = car.getInt("car_id");
                                    String carImage = car.getString("car_image");
                                    String carMatricule = car.getString("matricule");
                                    double latitude = car.getDouble("latitude");
                                    double longitude = car.getDouble("longitude");

                                    Car c = new Car(carId,latitude,longitude, carImage,driver,carMatricule );
                                    carsList.add(c);
                                }
                                SearchAdapter adapter = new SearchAdapter(SearchActivity.this,carsList);
                                gridViewSearch.setAdapter(adapter);
                                pdDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            pdDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
                Toast.makeText(SearchActivity.this,"Registration Error !2"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("search",editTextSearch.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}