package com.example.owner_pc.androidkanazawa2015;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.owner_pc.androidkanazawa2015.gnavi.AsyncTaskCallbacks;
import com.example.owner_pc.androidkanazawa2015.gnavi.GnaviCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.Position;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncTaskCallbacks, ViewPager.OnPageChangeListener{

    private MainFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private GnaviCtrl gnaviCtrl = new GnaviCtrl(this, this);
    private SettingButton settingButton = new SettingButton(this);
    private double latitude  = 36.594682;
    private double longitude = 136.625573;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //位置情報の読み込み
        //getLocation();
        Position position = new Position(latitude, longitude);
        //ぐるナビの読み込み
        gnaviCtrl.execute(position);

        //final ListView listView = (ListView)findViewById(R.id.list);
    }

    private void updateFragment(){
        int[] data = new int[5];
        for(int i=0;i<5;++i){
            data[i] = i+5;
        }
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);

        pagerAdapter.destroyAllItem(viewPager);
        pagerAdapter.setData(data);
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
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);
        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this, latitude, longitude, new ShopCtrl());
        pagerAdapter.setData(data);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        //ボタンの表示
        settingButton.onDrawButton();

        Button updateButton = (Button) findViewById(R.id.update_button);
//        updateButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        //読み込み完了後スプラッシュ画面閉じる
//        setTheme(R.style.AppTheme_NoActionBar);


        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                updateFragment();
            }
        });
    }

    //ぐるナビ読み込み失敗
    @Override
    public void onTaskCancelled(){

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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.d("MainActivity", "onPageScrolled() position="+position);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("MainActivity", "onPageSelected() position="+position);
        ImageButton rightButton = settingButton.getRightButton();
        ImageButton leftButton  = settingButton.getLeftButton();
        if(rightButton != null && leftButton != null) {
            if (position == 1) {
                //マップ画面時透過(灰色重ねる)
                rightButton.setEnabled(false);
                leftButton.setEnabled(false);
                rightButton.setAlpha(0.5f);
                leftButton.setAlpha(0.5f);
                rightButton.setColorFilter(0xaa808080);
                leftButton.setColorFilter(0xaa808080);
            } else {
                //リスト、ルーレット画面では通常表示
                rightButton.setEnabled(true);
                leftButton.setEnabled(true);
                rightButton.setAlpha(1.0f);
                leftButton.setAlpha(1.0f);
                rightButton.setColorFilter(BIND_NOT_FOREGROUND);
                leftButton.setColorFilter(BIND_NOT_FOREGROUND);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        Log.d("MainActivity", "onPageScrollStateChanged() state="+state);
    }

    //位置情報の取得
    public void getLocation() {
        // LocationManagerを取得
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Criteriaオブジェクトを生成
        Criteria criteria = new Criteria();
        //位置情報の精度
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        //消費電力
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //ロケーションプロバイダの取得
        String provider = locationManager.getBestProvider(criteria, true);
        //現在地取得
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
}
