package com.taxilik.client.home.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.taxilik.History;
import com.taxilik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ClientHistoryAdapter extends BaseAdapter {
    private Context context ;
    private ArrayList<History> histories;

    public ClientHistoryAdapter(Context context, ArrayList<History> histories) {
        this.context = context;
        this.histories = histories;
    }

    @Override
    public int getCount() {
        return histories.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int position = i ;
        View row;
        LayoutInflater inflater =LayoutInflater.from(context);
        row = inflater.inflate(R.layout.layout_client_history,viewGroup,false);

        TextView textViewDriverName = row.findViewById(R.id.client_history_driver_name);
        TextView textViewMatricule = row.findViewById(R.id.client_history_car_matricule);
        TextView textViewDateStart = row.findViewById(R.id.client_history_date_start);

        ImageView carImage =  row.findViewById(R.id.client_history_car_image);
        ImageView driverImage =  row.findViewById(R.id.client_history_driver_image);

        textViewDriverName.setText(histories.get(i).getDriver().getFullName());
        textViewMatricule.setText(histories.get(i).getCar().getMatricule());
        String date ="";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpleDateFormat.format(histories.get(i).getDateStart());
        textViewDateStart.setText(date);

        Picasso.get().load(histories.get(i).getCar().getImage()).into(carImage);
        Picasso.get().load(histories.get(i).getDriver().getImage()).into(driverImage);

        return row;
    }
}
