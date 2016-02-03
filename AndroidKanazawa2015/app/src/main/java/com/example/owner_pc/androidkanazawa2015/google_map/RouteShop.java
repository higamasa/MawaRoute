package com.example.owner_pc.androidkanazawa2015.google_map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.owner_pc.androidkanazawa2015.gnavi.Position;

/**
 * Created by nns on 2016/01/15.
 */
public class RouteShop {
    private Activity activity;

    public RouteShop(Activity activity){
        this.activity = activity;
    }

    public void Route(Position start, Position goal) {

        // インテント設定
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + start.latitude + "," + start.longitude + "&daddr=" + goal.latitude + "," + goal.longitude ));
        activity.startActivity(intent);
    }
}
