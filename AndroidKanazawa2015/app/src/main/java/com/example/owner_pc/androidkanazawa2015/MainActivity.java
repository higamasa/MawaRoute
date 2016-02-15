package com.example.owner_pc.androidkanazawa2015;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
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
import com.example.owner_pc.androidkanazawa2015.list.List;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopList;


public class MainActivity extends AppCompatActivity implements AsyncTaskCallbacks,List.FragmentTopCallback,LocationListener{

    private MainFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private GnaviCtrl gnaviCtrl = new GnaviCtrl(this, this);
    private SettingButton settingButton = new SettingButton(this);
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

    private void updateFragment(){
        int[] data = new int[5];
        for(int i=0;i<5;++i){
            data[i] = i+5;
        }
//        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);

        pagerAdapter.destroyAllItem(viewPager);
        pagerAdapter.setData(data);
        pagerAdapter.setShopCtrl(new ShopCtrl());
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(pagerAdapter);
    }

    //設定ActivityのCallBackで呼ぶ
    private void updateListFragment(ShopList shopList){
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

        //todo お試しデータ引継ぎ
        int[] data = new int[5];
        for(int i=0;i<5;++i){
            data[i] = i;
            System.out.println(data[i]);
        }

        //TabとSwipeの読み込み
        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);
        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this, latitude, longitude, new ShopCtrl());
        pagerAdapter.setData(data);
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
//        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        //ボタンの表示
//        settingButton.onDrawButton();

//        Button updateButton = (Button) findViewById(R.id.update_button);
//        updateButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        //読み込み完了後スプラッシュ画面閉じる
//        setTheme(R.style.AppTheme_NoActionBar);

//        updateButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                updateFragment();
//            }
//        });
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

//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.d("MainActivity", "onPageScrolled() position="+position);
//    }


//    @Override
//    public void onPageSelected(int position) {
//        Log.d("MainActivity", "onPageSelected() position="+position);
//        ImageButton rightButton = settingButton.getRightButton();
//        ImageButton leftButton  = settingButton.getLeftButton();
//        if(rightButton != null && leftButton != null) {
//            if (position == 1) {
//                //マップ画面時透過(灰色重ねる)
//                rightButton.setEnabled(false);
//                leftButton.setEnabled(false);
//                rightButton.setAlpha(0.5f);
//                leftButton.setAlpha(0.5f);
//                rightButton.setColorFilter(0xaa808080);
//                leftButton.setColorFilter(0xaa808080);
//            } else {
//                //リスト、ルーレット画面では通常表示
//                rightButton.setEnabled(true);
//                leftButton.setEnabled(true);
//                rightButton.setAlpha(1.0f);
//                leftButton.setAlpha(1.0f);
//                rightButton.setColorFilter(BIND_NOT_FOREGROUND);
//                leftButton.setColorFilter(BIND_NOT_FOREGROUND);
//            }
//        }
//    }

//    @Override
//    public void onPageScrollStateChanged(int state) {
//        Log.d("MainActivity", "onPageScrollStateChanged() state="+state);


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
    public void listCallback(ShopParameter shopParameter, boolean bool) {

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
