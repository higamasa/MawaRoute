package kies.mawaroute.google_map;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import kies.mawaroute.R;
import kies.mawaroute.gnavi.ShopParameter;

/**
 * Created by atsusuke on 2016/01/29
 * 村田篤亮.
 */
public class Map extends Fragment implements View.OnClickListener, OnMapReadyCallback {
    View view;
    private SupportMapFragment fragment;
    private GoogleMap mMap;
    private boolean flag = false;
    private FloatingActionButton mFab;
    private ArrayList<ShopParameter> shopList = new ArrayList<ShopParameter>();
    private ArrayList<Marker> setMarker = new ArrayList<Marker>();
    private MarkerOptions options = new MarkerOptions();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Fabボタンセット
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setRippleColor(getResources().getColor(R.color.colorWhite));
        mFab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(83, 53, 99)));
        fabSettings(flag);
        this.mFab.setOnClickListener(this);
        //マップの初期化
        android.support.v4.app.FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
    }

    private void MakerSetting(LatLng lat) {
        // 緯度・経度
        options.position(lat);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        // マーカーを貼り付け
        setMarker.add(mMap.addMarker(options));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bundle bundle = getArguments();
        double latitude = bundle.getDouble("latitude");
        double longitude = bundle.getDouble("longitude");
        LatLng lat = new LatLng(latitude, longitude);
        options = new MarkerOptions();
        // 緯度・経度
        options.position(lat);
        // タイトル・スニペット
        options.title("現在地");
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.here);
        // アイコン(マップ上に表示されるデフォルトピン)
        options.icon(BitmapDescriptorFactory.fromBitmap(bitmap.createScaledBitmap(bitmap, 150, 150, true)));
        // マーカーを貼り付け
        googleMap.addMarker(options);
        // 地図の表示位置を指定する。
        CameraUpdate camera = CameraUpdateFactory
                .newCameraPosition(new CameraPosition.Builder()
                        .target(lat)
                        .zoom(15).build());
        googleMap.moveCamera(camera);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
        MapUiSettings();
    }

    @Override
    public void onClick(View v) {
        if (flag == false) {
            this.flag = true;
            MapUiSettings();
        } else {
            this.flag = false;
            MapUiSettings();
        }
        fabSettings(flag);
    }

    private void MapUiSettings() {
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

    private void editShopList(ShopParameter shop, boolean flag) {
        if (flag) {
            //リストに同じ店がないので追加する
            shopList.add(shop);
        } else {
            //リストに同じ店があるので排除する
            for (int i = 0; i < shopList.size(); ++i) {
                String shopName = shopList.get(i).getShopName();
                if (shopName.equals(shop.getShopName())) {
                    shopList.remove(i);
                    return;
                }
            }
        }
    }

    //MainActivityから選択された店の情報を受け取り、追加判定をする
    public void setShopParameter(ShopParameter shop, boolean shopflag) {
        editShopList(shop, shopflag);
        if (shopflag == true) {
            options.title(shop.getShopName());
            MakerSetting(new LatLng(shop.getLatitude(), shop.getLongitude()));
        } else {
            MarkerDelete();
            for (int i = 0; i < shopList.size(); i++) {
                options.title(shopList.get(i).getShopName());
                MakerSetting(new LatLng(shopList.get(i).getLatitude(), shopList.get(i).getLongitude()));
            }
        }
    }

    private void MarkerDelete() {
        for (int i = 0; i < setMarker.size(); i++) {
            setMarker.get(i).remove();
        }
        setMarker.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MarkerDelete();
        options.icon(null);
        mMap = null;
        view = null;
        options = null;
        fragment = null;
    }

    private void fabSettings(boolean flag) {
        Log.d("check", String.valueOf(flag));
        if (flag == false) {
            final Bitmap bitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.osipin_pined_r));
            mFab.setImageBitmap((bitmap.createScaledBitmap(bitmap, 100, 100, true)));
        } else {
            final Bitmap bitmap = (BitmapFactory.decodeResource(getResources(), R.drawable.osipin_unpin_r_r));
            mFab.setImageBitmap((bitmap.createScaledBitmap(bitmap, 100, 100, true)));
        }
    }
}

