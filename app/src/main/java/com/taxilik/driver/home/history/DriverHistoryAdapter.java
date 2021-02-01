package com.taxilik.driver.home.history;

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

public class DriverHistoryAdapter extends BaseAdapter {
    private Context context ;
    private ArrayList<History> histories;

    public DriverHistoryAdapter(Context context, ArrayList<History> histories) {
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
        View row;
        LayoutInflater inflater =LayoutInflater.from(context);
        row = inflater.inflate(R.layout.layout_driver_history,viewGroup,false);

        TextView textViewClientName = row.findViewById(R.id.driver_history_client_name);
        TextView textViewDateStart = row.findViewById(R.id.driver_history_date_start);
        ImageView clientImage =  row.findViewById(R.id.driver_history_client_image);

        textViewClientName.setText(histories.get(i).getClient().getFullName());
        String date ="";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpleDateFormat.format(histories.get(i).getDateStart());
        textViewDateStart.setText(date);

        Picasso.get().load(histories.get(i).getClient().getImage()).into(clientImage);

        return row;
    }
}
