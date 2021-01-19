package com.taxilik.client.home.offre;

import android.content.Context;
import android.os.Bundle;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taxilik.Car;
import com.taxilik.ClientActivity;
import com.taxilik.R;
import com.taxilik.client.home.ClientHomeFragment;
import com.taxilik.client.home.map.ClientMapFragment;
import com.taxilik.client.home.ClientHomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.taxilik.Data.CurrentUser;

public class OffreAdapter extends BaseAdapter {
    Context context;
    ArrayList<Car> carsList;

    FirebaseFirestore db ;

    private Fragment fragment;


    public OffreAdapter(Context context,  ArrayList<Car> carsList,Fragment fragment) {
        this.context=context;
        this.carsList=carsList;
        db = FirebaseFirestore.getInstance();
        this.fragment=fragment;
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
                // chaymae

                //envoyer les donnes
                Bundle infob= new Bundle();
                infob.putString("Chauffeurname",carsList.get(position).getDrivername());
                infob.putString("matricule",carsList.get(position).getMatDisc());

                //ok
                  ClientHomeFragment f = (ClientHomeFragment) fragment;
                  f.Open(infob);


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
        onlineOrder.put("ClientID", CurrentUser.getId());
        onlineOrder.put("CarID", c.getCarID());


        db.collection("OnlineOrder").document(CurrentUser.getId() +"-"+c.getCarID())
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
                        Log.e("gg",e.getMessage());
                    }
                });

    }

    private void cancelRequest(final Car c) {
        db.collection("OnlineOrder").document(CurrentUser.getId() +"-"+c.getCarID())
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
                        Log.e("gg",e.getMessage());
                    }

                });
    }

}
