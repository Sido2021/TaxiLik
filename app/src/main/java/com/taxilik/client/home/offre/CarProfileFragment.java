package com.taxilik.client.home.offre;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;
import com.taxilik.Car;
import com.taxilik.Data;
import com.taxilik.DirectionParser;
import com.taxilik.Feedback;
import com.taxilik.R;
import com.taxilik.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CarProfileFragment extends DialogFragment {

    private final static int MY_PERMISSIONS_REQUEST = 32;

    GoogleMap map ;
    Car car ;
    Location currentLocation ;
    TextView driverName,carMatricule , textViewDistance , textViewTime ,textViewPrice;
    ImageView carImage , driverImage;
    Button showHideFeedback ;
    LinearLayout layoutFeedBack ;
    ImageView sendFeedBack ;
    EditText editTextFeedBack ;
    ListView listViewFeedBack ;
    boolean feedBackVisible = false;
    String URL_FEEDBACK = "https://omega-store.000webhostapp.com/getFeedback.php";
    String URL_POST_FEEDBACK = "https://omega-store.000webhostapp.com/postFeedback.php";

    FeedbackAdapter adapter ;
    ArrayList<Feedback> feedbacksList = new ArrayList<>();

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            createTestLocation();
        }
    };
    private void createTestLocation() {

        LatLng clientLatLng = new LatLng(34.402108422360705,-2.8970004531777307);
        MarkerOptions clientMarkerOptions = new MarkerOptions().position(clientLatLng).title("My Test Location");

        LatLng carLatLng = new LatLng(car.getLatitude(),car.getLongitude());
        MarkerOptions carMarkerOptions = new MarkerOptions().position(carLatLng).title("Mat :" + car.getMatricule());
        carMarkerOptions.icon(bitmapDescriptorFromVector(getContext(),R.drawable.ic_car_gold));


        map.animateCamera(CameraUpdateFactory.newLatLngZoom(clientLatLng,15));
        map.addMarker(clientMarkerOptions);
        map.addMarker(carMarkerOptions);

        map.addCircle(new CircleOptions()
                .center(new LatLng(clientLatLng.latitude, clientLatLng.longitude))
                .radius(1000).strokeWidth(2)
                .strokeColor(Color.BLUE)
                .fillColor(0x3f0000ff));

        new TaskDirectionRequest().execute(buildRequestUrl(clientLatLng,carLatLng));

        Location l1 = new Location("");
        Location l2 = new Location("");
        l1.setLatitude(clientLatLng.latitude);
        l1.setLongitude(clientLatLng.longitude);

        l2.setLatitude(carLatLng.latitude);
        l2.setLongitude(carLatLng.longitude);

        double distance = Math.floor(l1.distanceTo(l2)*100)/100 ;
        textViewDistance.setText(String.valueOf(distance).concat(" m"));

        int s = (int) Math.floor(distance/60000*3600); // v : 60km
        int h = s/60;
        s%=60;
        int m = s/60;
        s%=60;

        textViewTime.setText(String.valueOf(h+"h"+m+"m"+s+"s"));

    }


    public CarProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,R.style.AppTheme);
        if (getArguments() != null) {
            car =(Car) getArguments().getSerializable("car");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_car_profile,container,false);
        driverName = v.findViewById(R.id.car_profile_driver_name);
        driverImage = v.findViewById(R.id.car_profile_driver_image);
        carMatricule = v.findViewById(R.id.car_profile_car_matricule);
        carImage = v.findViewById(R.id.car_profile_car_image);
        textViewDistance = v.findViewById(R.id.car_profile_distance);
        textViewTime = v.findViewById(R.id.car_profile_time);
        textViewPrice = v.findViewById(R.id.car_profile_price);

        listViewFeedBack = v.findViewById(R.id.car_profile_list_view_feedback);
        editTextFeedBack = v.findViewById(R.id.car_profile_edit_text_feedback);
        sendFeedBack = v.findViewById(R.id.car_profile_send_feedback);
        showHideFeedback = v.findViewById(R.id.car_profile_show_hide_feedback);
        layoutFeedBack = v.findViewById(R.id.car_profile_layout_feedback);
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        driverName.setText(car.getDriver().getFullName());
        carMatricule.setText(car.getMatricule());
        Picasso.get().load(car.getImage()).into(carImage);
        Picasso.get().load(car.getDriver().getImage()).into(driverImage);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(callback);

        showHideFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!feedBackVisible){
                    layoutFeedBack.setVisibility(View.VISIBLE);
                    feedBackVisible = true;
                    showHideFeedback.setText("Hide Feedback");
                }
                else {
                    layoutFeedBack.setVisibility(View.GONE);
                    feedBackVisible = false;
                    showHideFeedback.setText("Show Feedback");
                }
            }
        });

        loadFeedBack();

        sendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextFeedBack.getText().toString().isEmpty()){
                    sendFeedBack();
                }
            }
        });
        return v;
    }

    private void sendFeedBack() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_POST_FEEDBACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("0")){
                                java.util.Date date = Calendar.getInstance().getTime();
                                Feedback f = new Feedback(0,car.getCarID(),Data.CurrentUser,editTextFeedBack.getText().toString(), date);
                                feedbacksList.add(f);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Feedback sent ", Toast.LENGTH_SHORT).show();
                                editTextFeedBack.setText("");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error !", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("car_id",String.valueOf(car.getCarID()));
                params.put("user_id", String.valueOf(Data.CurrentUser.getId()));
                params.put("feedback_text",editTextFeedBack.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void loadFeedBack() {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FEEDBACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray feedbacksArray = jsonObject.getJSONArray("feedbacks");

                            if(success.equals("0")){
                                feedbacksList = new ArrayList<>();
                                for (int i =0;i<feedbacksArray.length();i++){
                                    JSONObject feedback = feedbacksArray.getJSONObject(i);

                                    int userId = feedback.getInt("user_id");
                                    String firstName = feedback.getString("first_name");
                                    String lastName = feedback.getString("last_name");
                                    String phone = feedback.getString("phone");
                                    String image = feedback.getString("user_image");
                                    int type = feedback.getInt("type");

                                    User user = new User(userId,firstName,lastName,phone,image,type,"");

                                    int feedBackId = feedback.getInt("feedback_id");
                                    String feedBackText = feedback.getString("feedback_text");
                                    Date feedbackDate = Date.valueOf(feedback.getString("feedback_date"));

                                    Feedback f = new Feedback(feedBackId,car.getCarID(),user,feedBackText,feedbackDate);
                                    feedbacksList.add(f);
                                }
                                adapter=new FeedbackAdapter(getContext(),feedbacksList);
                                listViewFeedBack.setAdapter(adapter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"Registration Error !1"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Registration Error !2"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("car_id",String.valueOf(car.getCarID()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private String buildRequestUrl(LatLng origin, LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";

        String param = strOrigin + "&" + strDestination + "&" + sensor + "&" + mode;
        String output = "json";
        String APIKEY = getResources().getString(R.string.google_maps_key);

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + "&key="+APIKEY;
        return url;
    }

    private String requestDirection(String requestedUrl) {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(requestedUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        httpURLConnection.disconnect();
        return responseString;
    }

    //Get JSON data from Google Direction
    public class TaskDirectionRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            TaskParseDirection parseResult = new TaskParseDirection();
            parseResult.execute(responseString);
        }
    }

    public class TaskParseDirection extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonString) {
            List<List<HashMap<String, String>>> routes = null;
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(jsonString[0]);
                DirectionParser parser = new DirectionParser();
                routes = parser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lng"));

                    points.add(new LatLng(lat, lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15f);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }
            if (polylineOptions != null) {
                map.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getContext(), "Direction not found", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{permission},
                    MY_PERMISSIONS_REQUEST);
        }

    }
}