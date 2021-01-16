package com.taxilik.client.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taxilik.R;
import com.taxilik.client.home.ClientHomeFragment;

import static com.taxilik.Data.CurrentUser;

public class ClientProfileFragment extends Fragment {

    TextView fullname_field,username_field,fname_prof,lname_prof,email_prof,phone_prof;
    ;
    Button edit_profile;

    private ClientHomeFragment.OnFragmentInteractionListener mListener;

    private void setEditText(String nom,String prenom,String email,String phone){
        fullname_field.setText(nom.concat(" "+prenom));
        username_field.setText(nom.concat("_"+prenom+"@"));
        fname_prof.setText(nom);
        lname_prof.setText(prenom);
        email_prof.setText(email);
        phone_prof.setText(phone);
    }


    public ClientProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
                Intent intent=new Intent(getContext(),ClientProfileFragmentEdit.class);
                startActivity(intent);
            }
        });

        setEditText(CurrentUser.getFirstName(),CurrentUser.getLastName(),CurrentUser.getEmail(),CurrentUser.getPhone());
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