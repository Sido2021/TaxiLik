package com.taxilik.client.home.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.taxilik.History;
import com.taxilik.R;

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

        TextView textViewDriverName = row.findViewById(R.id.driver_name);
        TextView textViewCarId = row.findViewById(R.id.car_id);
        CheckBox checkBox = row.findViewById(R.id.check_box);

        textViewDriverName.setText(histories.get(i).getDriver().getFullName());
        textViewCarId.setText(histories.get(i).getCar().getCarID()+"");

        return row;
    }
}
