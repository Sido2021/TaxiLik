package com.taxilik;

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
import com.squareup.picasso.Picasso;
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

public class SearchAdapter extends BaseAdapter {
    Context context;
    ArrayList<Car> carsList;

    public SearchAdapter(Context context,  ArrayList<Car> carsList) {
        this.context=context;
        this.carsList=carsList;
        Toast.makeText(context, carsList.size()+"", Toast.LENGTH_SHORT).show();
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
        row = inflater.inflate(R.layout.search_layout,parent,false);

        TextView driverName=row.findViewById(R.id.search_driver_name);
        TextView carMatriculle= row.findViewById(R.id.search_car_matricule);

        ImageView carImage=row.findViewById(R.id.search_car_image);
        ImageView driverImage=row.findViewById(R.id.search_driver_image);

        driverName.setText(carsList.get(position).getDriver().getFullName());
        carMatriculle.setText("Mat : "+carsList.get(position).getMatricule());

        Picasso.get().load(carsList.get(position).getImage()).into(carImage);
        Picasso.get().load(carsList.get(position).getDriver().getImage()).into(driverImage);

        return row;
    }
}
