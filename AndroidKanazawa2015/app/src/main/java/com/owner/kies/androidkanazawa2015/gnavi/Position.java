package com.owner.kies.androidkanazawa2015.gnavi;

import java.io.Serializable;

/**
 * Created by owner-PC on 2016/01/15.
 */
public class Position implements Serializable {
    public double latitude;
    public double longitude;

    public Position() {
    }

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
