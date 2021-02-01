package com.taxilik;

import java.util.Date;

public class Feedback {
    private int feedbackId  ;
    private int carId  ;
    private User user ;
    private String feedbackText ;
    private Date date ;

    public Feedback(int feedbackId, int carId, User user, String feedbackText, Date date) {
        this.feedbackId = feedbackId;
        this.carId = carId;
        this.user = user;
        this.feedbackText = feedbackText;
        this.date = date;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public int getCarId() {
        return carId;
    }

    public User getUser() {
        return user;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public Date getDate() {
        return date;
    }
}
