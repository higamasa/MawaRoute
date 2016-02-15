package com.example.owner_pc.androidkanazawa2015;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.owner_pc.androidkanazawa2015.gnavi.AsyncTaskCallbacks;
import com.example.owner_pc.androidkanazawa2015.gnavi.GnaviCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.Position;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopParameter;
import com.example.owner_pc.androidkanazawa2015.google_map.Map;
import com.example.owner_pc.androidkanazawa2015.list.List;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopList;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AsyncTaskCallbacks,List.FragmentTopCallback,LocationListener{

    private MainFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private GnaviCtrl gnaviCtrl = new GnaviCtrl(this, this);
    private LocationManager locationManager;
    private double latitude  = 36.594682;
    private double longitude = 136.625573;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //位置情報の読み込み
        getLocation();
    }

    //ListFragmentを再生成して絞り込み条件を反映する
    private void updateListFragment(ArrayList<ShopParameter> shopList){
        viewPager.setOffscreenPageLimit(2);
        pagerAdapter.destroyListItem(viewPager);
        pagerAdapter.setShopList(shopList);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(pagerAdapter);
    }

    //ぐるナビ読み込み完了
    @Override
    public void onTaskFinished(){

        //TabとSwipeの読み込み
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this, latitude, longitude, new ShopCtrl().getShopList());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.list_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.map_tab);
        tabLayout.getTabAt(2).setIcon(R.drawable.cir_gray);
        tabLayout.getTabAt(0).setText("List");
        tabLayout.getTabAt(1).setText("Map");
        tabLayout.getTabAt(2).setText("Roulette");
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    //ぐるナビ読み込み失敗
    @Override
    public void onTaskCancelled() {

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.v("ActivityMain", "onPause()");

        // "プログレスダイアログ表示（シンプル）" のスレッド、プログレスダイアログが存在する場合
        if (this.gnaviCtrl != null &&
                this.gnaviCtrl.progressDialog != null) {

            Log.v("DialogShowing", String.valueOf(this.gnaviCtrl.progressDialog.isShowing()));

            // プログレスダイアログ表示中の場合
            if (this.gnaviCtrl.progressDialog.isShowing()) {

                // プログレスダイアログを閉じる
                this.gnaviCtrl.progressDialog.dismiss();
            }
        }
    }

    //位置情報の取得
    public void getLocation() {

        //位置情報がオンになっているかの確認
        checkGpsSettings();

        // LocationManagerを取得
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Criteriaオブジェクトを生成
        Criteria criteria = new Criteria();
        //位置情報の精度
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        //消費電力
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //ロケーションプロバイダの取得
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final String[] permissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 0);
            return;
        }
        locationManager.requestLocationUpdates(provider, 0, 0, this);
    }

    @Override
    public void listCallback(ShopParameter shop, boolean flag) {
        //rouletteFragmentを取得し、店情報セット
        RoulettePage roulettePage = (RoulettePage)pagerAdapter.findFragmentByPosition(viewPager, 2);
        roulettePage.setShopParameter(shop, flag);
        //todo マップでも同様な処理をする(マップクラスでsetShopParameterと同義なメソッド作って)
        Map map = (Map)pagerAdapter.findFragmentByPosition(viewPager, 1);
        map.setShopParameter(shop, flag);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("check", String.valueOf(location.getLatitude()));
        Log.d("check", String.valueOf(location.getLongitude()));
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        //位置情報取得を破棄
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
        Position position = new Position(latitude, longitude);
        //ぐるナビの読み込み
        gnaviCtrl.execute(position);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    //GPSがオフの時、GPS設定画面を開く
    private boolean checkGpsSettings() {
        // 位置情報の設定の取得
        String gps = android.provider.Settings.Secure.getString(
                getContentResolver(),
                android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        // GPS機能か無線ネットワークがONになっているかを確認
        if (gps.indexOf("gps", 0) < 0 && gps.indexOf("network", 0) < 0) {
            // GPSサービスがOFFになっている場合、ダイアログを表示
            new AlertDialog.Builder(this)
                    .setTitle("位置情報の設定")
            .setMessage("位置情報の設定がOFFになっている為、アプリの機能がご利用いただけません。位置情報の設定をONに変更して下さい。")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // 位置情報設定画面へ移動する
                            Intent intent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            startActivity(intent);
                }
            })
                    .create()
                    .show();
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
