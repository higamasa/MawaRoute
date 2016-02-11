package com.example.owner_pc.androidkanazawa2015;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.owner_pc.androidkanazawa2015.gnavi.AsyncTaskCallbacks;
import com.example.owner_pc.androidkanazawa2015.gnavi.GnaviCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.Position;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;

public class MainActivity extends AppCompatActivity implements AsyncTaskCallbacks, ViewPager.OnPageChangeListener {

    private Toolbar toolbar = null;
    private SearchView mSearchView = null;
    private Menu menu = null;
    private GnaviCtrl gnaviCtrl = new GnaviCtrl(this, this);
    private SettingButton settingButton = new SettingButton(this);
    private double latitude = 36.594682;
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

        // ツールバー配置
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.search);
        //menu = (Menu)toolbar.getMenu().findItem(R.id.action_settings).getActionView();
        search();
    }

    //ぐるナビ読み込み完了
    @Override
    public void onTaskFinished() {
        //TabとSwipeの読み込み
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager()
                , MainActivity.this, latitude, longitude, new ShopCtrl()));
        viewPager.addOnPageChangeListener(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        //ボタンの表示
        //settingButton.onDrawButton();
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
        if (this.gnaviCtrl != null && this.gnaviCtrl.progressDialog != null) {

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
        Log.d("MainActivity", "onPageSelected() position=" + position);
        ImageButton rightButton = settingButton.getRightButton();
        ImageButton leftButton = settingButton.getLeftButton();
        if (rightButton != null && leftButton != null) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClassName("com.example.owner_pc.androidkanazawa2015", "com.example.owner_pc.androidkanazawa2015.range_category_settings");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void search(){
        mSearchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

}
