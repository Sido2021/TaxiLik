package com.taxilik;

import java.io.Serializable;

public class Car implements Serializable {
    private int carID ;
    private String image;
    private User driver;
    private String matricule;
    private double latitude , longitude ;
    private boolean ordred = false ;


    public Car(int carID , double latitude , double longitude, String image, User driver, String matricule){
        this.carID = carID ;
        this.driver=driver;
        this.image=image;
        this.matricule=matricule;
        this.latitude = latitude ;
        this.longitude = longitude;
    }

    public int getCarID() {
        return carID;
    }

    public String getImage() {
        return image;
    }

    public User getDriver() {
        return driver;
    }

    public String getMatricule() {
        return matricule;
    }

    public boolean isOrdred() {
        return ordred;
    }

    public void setOrdred(boolean ordred) {
        this.ordred = ordred;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
