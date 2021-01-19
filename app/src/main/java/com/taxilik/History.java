package com.taxilik;

import java.util.Date;

public class History {
    int requestId ;
    Car car;
    Date dateStart ,dateEnd ;
    User driver ;
    User client ;

    public History(int requestId, Car car, Date dateStart, Date dateEnd , User driver, User client) {
        this.requestId = requestId;
        this.car = car;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.driver = driver ;
        this.client = client ;
    }

    public User getClient() {
        return client;
    }

    public int getRequestId() {
        return requestId;
    }

    public User getDriver() {
        return driver;
    }

    public Car getCar() {
        return car;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }
}
