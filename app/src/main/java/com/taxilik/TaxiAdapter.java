package com.taxilik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TaxiAdapter extends ArrayAdapter {
    Context activityAdapter;
    int resource;
    ArrayList<CarsDipo> carsList;



    public TaxiAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CarsDipo> carsList) {
        super(context, resource, carsList);
        activityAdapter=context;
        this.resource=resource;
        this.carsList=carsList;
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        LayoutInflater rowinf=LayoutInflater.from(activityAdapter);
        row=rowinf.inflate(resource,parent,false);

        TextView txtName=row.findViewById(R.id.chauffxml);
        TextView txtMat= row.findViewById(R.id.matxml);
        ImageView txtImage=row.findViewById(R.id.imageXML);


        txtName.setText(carsList.get(position).drivername);
        txtMat.setText(carsList.get(position).matDisc);
        //txtImage.setImageResource(dra);

        return row;
    }
}
