package com.taxilik.client.home.offre;

import android.content.Context;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.taxilik.Feedback;
import com.taxilik.R;
import com.taxilik.client.home.ClientHomeFragment;
import com.taxilik.client.home.map.ClientMapFragment;
import com.taxilik.client.home.ClientHomeFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.taxilik.Data.CurrentUser;

public class FeedbackAdapter extends BaseAdapter {
    Context context;
    ArrayList<Feedback> feedbackList;

    public FeedbackAdapter(Context context,  ArrayList<Feedback> feedbackList) {
        this.context=context;
        this.feedbackList=feedbackList;
    }


    @Override
    public int getCount() {
        return feedbackList.size();
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
        row = inflater.inflate(R.layout.layout_feedback,parent,false);

        ImageView userImage = row.findViewById(R.id.feedback_user_image);
        TextView userName = row.findViewById(R.id.feedback_user_name);
        TextView feedbackText = row.findViewById(R.id.feedback_text_feedback);
        TextView feedbackDate = row.findViewById(R.id.feedback_text_date);

        feedbackText.setText(feedbackList.get(position).getFeedbackText());
        userName.setText(feedbackList.get(position).getUser().getFullName());

        String date ="";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpleDateFormat.format(feedbackList.get(position).getDate());
        feedbackDate.setText(date);

        Picasso.get().load(feedbackList.get(position).getUser().getImage()).into(userImage);

        return row;
    }

}
