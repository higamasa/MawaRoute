package com.example.owner_pc.androidkanazawa2015.google_map;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.owner_pc.androidkanazawa2015.R;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by atsusuke on 2016/01/29
 * 村田篤亮.
 */
public class Map extends Fragment implements View.OnClickListener {
    private SupportMapFragment fragment;
    private GoogleMap mMap;
    private int away = 2;
    private boolean flag = true;
    private FloatingActionButton mFab;
    MarkerOptions options = new MarkerOptions();
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //マップの初期化
        android.support.v4.app.FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mFab = (FloatingActionButton)view.findViewById(R.id.fab);
        this.mFab.setOnClickListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();
        double latitude = bundle.getDouble("latitude");
        double longitude = bundle.getDouble("longitude");
        ShopCtrl shopCtrl = (ShopCtrl)bundle.getSerializable("shopCtrl");
        LatLng lat = new LatLng(latitude, longitude);
        if (mMap == null) {
            mMap = fragment.getMap();
            MakerSetting(lat);
            for (int i=0; i < shopCtrl.getShopList().get(away).shop.size(); i++) {
                MakerSetting(new LatLng(shopCtrl.getShopList().get(away).shop.get(i).getLatitude(),
                        shopCtrl.getShopList().get(away).shop.get(i).getLongitude()));
            }
            // 地図の表示位置を指定する。
            CameraUpdate camera = CameraUpdateFactory
                    .newCameraPosition(new CameraPosition.Builder()
                            .target(lat)
                            .zoom(15).build());
            mMap.moveCamera(camera);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    marker.showInfoWindow();
                    return true;
                }
            });
            MapUiSettings();
            this.flag = true;
        }
    }
    private void MakerSetting(LatLng lat){
        // 緯度・経度
        options.position(lat);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        options.title(null);
        if (this.flag == true) {
            // タイトル・スニペット
            options.title("ワイはここにおるで！");
            //options.snippet(lat.toString());
            // アイコン(マップ上に表示されるデフォルトピン)
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            this.flag = false;
        }
        // マーカーを貼り付け
        mMap.addMarker(options);
    }
    @Override
    public void onClick(View v) {
        if (flag == true){
            MapUiSettings();
            this.flag = false;
        }else{
            MapUiSettings();
            this.flag = true;
        }
    }
    private void MapUiSettings(){
        // コンパスの有効化
        mMap.getUiSettings().setCompassEnabled(false);
        // 拡大・縮小ボタンを表示
        mMap.getUiSettings().setZoomControlsEnabled(false);
        // スクロールの有効化
        mMap.getUiSettings().setScrollGesturesEnabled(flag);
        // ズームの有効化
        mMap.getUiSettings().setZoomGesturesEnabled(flag);
        // 回転ジェスチャーの有効化
        mMap.getUiSettings().setRotateGesturesEnabled(flag);
    }
}

