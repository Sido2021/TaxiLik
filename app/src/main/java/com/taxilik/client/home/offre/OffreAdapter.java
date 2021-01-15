package com.taxilik.client.home.offre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taxilik.Car;
import com.taxilik.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OffreAdapter extends BaseAdapter {
    Context context;
    ArrayList<Car> carsList;
    String ClientID = "1" ;

    FirebaseFirestore db ;

    public OffreAdapter(Context context,  ArrayList<Car> carsList) {
        this.context=context;
        this.carsList=carsList;
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public int getCount() {
        return carsList.size();
    }

    @Nullable
    @Override
    public Car getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        LayoutInflater inflater =LayoutInflater.from(context);
        row = inflater.inflate(R.layout.car_offre_layout,parent,false);

        TextView txtName=row.findViewById(R.id.chauffxml);
        TextView txtMat= row.findViewById(R.id.matxml);

        ImageView txtImage=row.findViewById(R.id.imageXML);
        Button sendRequest = row.findViewById(R.id.send_request);
        Button  cancelRequest = row.findViewById(R.id.cancel_request);

        if(carsList.get(position).isOrdred()){
            sendRequest.setVisibility(View.GONE);
            cancelRequest.setVisibility(View.VISIBLE);
        }
        else {
            cancelRequest.setVisibility(View.GONE);
            sendRequest.setVisibility(View.VISIBLE);
        }

        txtName.setText(carsList.get(position).getDrivername());
        txtMat.setText(carsList.get(position).getMatDisc());
        //txtImage.setImageResource(dra);

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(carsList.get(position));
            }
        });
        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRequest(carsList.get(position));
            }
        });

        return row;
    }


    private void sendRequest(final Car c){
        Map<String, Object> onlineOrder = new HashMap<>();
        onlineOrder.put("ClientID", ClientID);
        onlineOrder.put("CarID", c.getCarID());


        db.collection("OnlineOrder").document(ClientID +"-"+c.getCarID())
                .set(onlineOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        c.setOrdred(true);
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    private void cancelRequest(final Car c) {
        db.collection("OnlineOrder").document(ClientID +"-"+c.getCarID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        c.setOrdred(false);
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}
