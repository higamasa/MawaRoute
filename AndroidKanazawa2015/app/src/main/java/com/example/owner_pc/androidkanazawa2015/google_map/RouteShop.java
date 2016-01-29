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


        // スタートの緯度経度を入れてください
        //String src_lat = "35.681382";   // 緯度
        //String src_ltg = "139.7660842"; // 経度
        // ゴールの緯度経度を入れてください
        //String des_lat = "35.684752";   // 緯度
        //String des_ltg = "139.707937";  // 経度

        // インテント設定
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + start.latitude + "," + start.longitude + "&daddr=" + goal.latitude + "," + goal.longitude ));
        activity.startActivity(intent);
    }
}
