package com.taxilik;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Car {
    private int carID ;
    private String image;
    private String drivername;
    private String matDisc;
    private boolean ordred = false ;


    public Car(int carID , String image, String drivername, String matDisc){
        this.carID = carID ;
        this.drivername=drivername;
        this.image=image;
        this.matDisc=matDisc;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDrivername() {
        return drivername;
    }

    public void setDrivername(String drivername) {
        this.drivername = drivername;
    }

    public String getMatDisc() {
        return matDisc;
    }

    public void setMatDisc(String matDisc) {
        this.matDisc = matDisc;
    }

    public boolean isOrdred() {
        return ordred;
    }

    public void setOrdred(boolean ordred) {
        this.ordred = ordred;
    }




}
