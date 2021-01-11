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

import java.lang.reflect.Array;

public class TaxiAdapter extends ArrayAdapter {
    Context activityAdapter;
    int resource;
    CarsDipo[] taxi;



    public TaxiAdapter(@NonNull Context context, int resource, @NonNull CarsDipo[] objects) {
        super(context, resource, objects);
        activityAdapter=context;
        this.resource=resource;
        taxi=objects;
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

        TextView txtName=(TextView) row.findViewById(R.id.chauffxml);
        TextView txtMat=(TextView) row.findViewById(R.id.matxml);
        ImageView txtImage=(ImageView) row.findViewById(R.id.imageXML);
        CarsDipo taxiposition=taxi[position];
        //put value in each item
        txtName.setText(taxiposition.drivername);
        txtName.setText(taxiposition.matDisc);
        int imgId=activityAdapter.getResources().getIdentifier(taxiposition.image,"drawable",activityAdapter.getPackageName());
        txtImage.setImageResource(imgId);




return row;
    }
}
