package com.example.owner_pc.androidkanazawa2015;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by nns on 2016/01/15.
 */
public class RouteShop extends Activity {


    public void Route(String src_lat, String src_ltg, String des_lat, String des_ltg) {
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
        intent.setData(Uri.parse("http://maps.google.com/maps?saddr=" + src_lat + "," + src_ltg + "&daddr=" + des_lat + "," + des_ltg));
        startActivity(intent);
    }
}
