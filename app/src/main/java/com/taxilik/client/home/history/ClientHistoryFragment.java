package com.taxilik.client.home.history;

import android.app.AlertDialog;
import android.content.ContentProviderClient;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.taxilik.R;
import com.taxilik.client.home.ClientHomeFragment;

import java.lang.reflect.Field;

public class ClientHistoryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CheckBox [] boxItem=new CheckBox[3];
    CheckBox princip;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientHistoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ClientHistoryFragment newInstance(String param1, String param2) {
        ClientHistoryFragment fragment = new ClientHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_client_history, container, false);
        princip=(CheckBox) v.findViewById(R.id.checkBox);
        for (int i = 0; i < boxItem.length; i++) { boxItem[i] = (CheckBox) v.findViewById(getIdByName("check_" + (i + 1))); }

        princip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getContext(), "Elemnts va supprimer", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < boxItem.length; i++) {boxItem[i].setVisibility(View.VISIBLE); boxItem[i].setChecked(true); }
                }else{

                    for (int i = 0; i < boxItem.length; i++) {
                        boxItem[i].setChecked(false); }

                }

            }
        });






     /*   for (int i = 0; i < boxItem.length; i++) {
            boxItem[i] = (CheckBox) v.findViewById(getIdByName("check_" + (i + 1)));
            boxItem[i].setVisibility(View.VISIBLE);
            boxItem[i].setChecked(true);
        }*/
        // Inflate the layout for this fragment
        return v;
    }

    public static int getIdByName(final String name) {

        try {

            final Field field = R.id.class.getDeclaredField(name);

            field.setAccessible(true);
            return field.getInt(null);

        } catch (Exception ignore) {
            return -1;
        }
    }


}