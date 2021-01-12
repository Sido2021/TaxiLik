package com.taxilik.client.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taxilik.R;
import com.taxilik.client.home.ClientHomeFragment;
import com.taxilik.client.home.history.ClientHistoryFragment;
import com.taxilik.client.home.map.ClientMapFragment;
import com.taxilik.client.home.offre.ClientOffreFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClientProfileFragment extends Fragment {
    String URL_host = "https://omega-store.000webhostapp.com/getProfile.php";

    TextView fullname_field,username_field,fname_prof,lname_prof,email_prof,phone_prof;
    ;
    Button edit_profile;

    FirebaseAuth mAuth ;
    FirebaseUser currentUser ;
    ProgressDialog pdDialog;

    private ClientHomeFragment.OnFragmentInteractionListener mListener;

    private void setEditText(String nom,String prenom,String email,String phone){
        fullname_field.setText(nom+" "+prenom);
        username_field.setText(nom+"_"+prenom+"@");
        fname_prof.setText(nom);
        lname_prof.setText(prenom);
        email_prof.setText(email);
        phone_prof.setText(phone);
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ClientProfileFragment() {
        // Required empty public constructor
    }

    public static ClientHomeFragment newInstance(String param1, String param2) {
        ClientHomeFragment fragment = new ClientHomeFragment();
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        return inflater.inflate(R.layout.client_profile_fragment, container, false);
    }



    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        fullname_field=view.findViewById(R.id.fullname_field);
        username_field=view.findViewById(R.id.username_field);
        fname_prof=view.findViewById(R.id.fname_prof);
        lname_prof=view.findViewById(R.id.lname_prof);
        email_prof=view.findViewById(R.id.email_prof);
        phone_prof=view.findViewById(R.id.phone_prof);
        edit_profile=view.findViewById(R.id.edit_profile);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intant=new Intent(getContext(),ClientProfileFragmentEdit.class);
                startActivity(intant);
                return;
            }
        });
        pdDialog= new ProgressDialog(getContext());
        pdDialog.setTitle("Login please wait...");
        pdDialog.setCancelable(false);

        profile();

    }

    private void profile()
    {
        pdDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_host,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");


                            if(success.equals("0")){

                                setEditText(jsonObject.getString("first_name"),jsonObject.getString("last_name"), currentUser.getEmail(),jsonObject.getString("phone"));
                            }
                            else{
                                setEditText("Unknown","Unknown", "Unknown","Unknown");

                            }

                            pdDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(),"profile error 1-"+e,Toast.LENGTH_LONG).show();
                            pdDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"profile error 2-"+error,Toast.LENGTH_LONG).show();
                pdDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("id",currentUser.getUid());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ClientHomeFragment.OnFragmentInteractionListener) {
            mListener = (ClientHomeFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromParentFragment(Uri uri);
    }

}