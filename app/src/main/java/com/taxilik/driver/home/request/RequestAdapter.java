package com.taxilik.driver.home.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;
import com.taxilik.Car;
import com.taxilik.LoginActivity2;
import com.taxilik.R;
import com.taxilik.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.taxilik.Data.CurrentUser;

public class RequestAdapter extends BaseAdapter {

    private Context context ;
    private ArrayList<User> clients ;
    private FirebaseFirestore db ;

    public RequestAdapter(Context context , ArrayList<User> clients){
        this.context = context ;
        this.clients = clients ;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return clients.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        final int position = i ;
        View row;
        LayoutInflater inflater =LayoutInflater.from(context);
        row = inflater.inflate(R.layout.layout_request,parent,false);

        TextView textViewNameRequest = row.findViewById(R.id.text_view_name_request);
        TextView textViewDistanceRequest = row.findViewById(R.id.text_view_distance_request);
        ImageView imageViewProfile = row.findViewById(R.id.image_view_request);
        Button buttonAcceptRequest = row.findViewById(R.id.button_accept_request);
        Button buttonRejectRequest = row.findViewById(R.id.button_reject_request);

        textViewNameRequest.setText(clients.get(i).getFullName());
        textViewDistanceRequest.setText(clients.get(i).getPhone());
        Picasso.get().load(clients.get(i).getImage()).into(imageViewProfile);

        buttonAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> onlineOrder = new HashMap<>();
                onlineOrder.put("ClientID", clients.get(position).getId() );
                onlineOrder.put("CarID", 1);
                onlineOrder.put("isAccepted", true);

                db.collection("OnlineOrder").document(clients.get(position).getId() +"-"+1)
                        .set(onlineOrder)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("gg",e.getMessage());
                            }
                        });

            }
        });
        buttonRejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("OnlineOrder").document(clients.get(position).getId() +"-"+1)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("gg",e.getMessage());
                    }
                });
            }
        });
        return row;
    }
}
