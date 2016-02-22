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
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.example.owner_pc.androidkanazawa2015.gnavi.AsyncTaskCallbacks;
import com.example.owner_pc.androidkanazawa2015.gnavi.GnaviCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.Position;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopParameter;
import com.example.owner_pc.androidkanazawa2015.google_map.Map;
import com.example.owner_pc.androidkanazawa2015.list.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncTaskCallbacks,List.FragmentTopCallback,LocationListener{

    private Toolbar _toolBar;
    private SearchView _searchView;
    private Menu menu = null;
    private MainFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private GnaviCtrl gnaviCtrl = new GnaviCtrl(this, this);
    private LocationManager locationManager;
    private double latitude  = 36.5299563;
    private double longitude = 136.6260366;
    private static final int SETTING_ACTIVITY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ツールバー配置
        _toolBar = (Toolbar)findViewById(R.id.tool_bar);
        _toolBar.setTitle("リスト");
        setSupportActionBar(_toolBar);
        //search();
        //位置情報の読み込み
        getLocation();
        //Position position = new Position(latitude, longitude);
        //gnaviCtrl.execute(position);
    }

    //ぐるナビ読み込み完了
    @Override
    public void onTaskFinished(){

        //TabとSwipeの読み込み
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        _toolBar.setTitle("リスト");
                        break;
                    case 1:
                        _toolBar.setTitle("マップ");
                        break;
                    case 2:
                        _toolBar.setTitle("ルーレット");
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(),
                getApplicationContext(), latitude, longitude, new ShopCtrl().getShopList());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.list_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.map_tab);
        tabLayout.getTabAt(2).setIcon(R.drawable.cir_gray);
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
        if (this.gnaviCtrl != null && this.gnaviCtrl.progressDialog != null) {

            Log.v("DialogShowing", String.valueOf(this.gnaviCtrl.progressDialog.isShowing()));

            // プログレスダイアログ表示中の場合
            if (this.gnaviCtrl.progressDialog.isShowing()) {

                // プログレスダイアログを閉じる
                this.gnaviCtrl.progressDialog.dismiss();
            }
        }
    }

    private void checkGpsSettings(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("位置情報の設定がOFFになっている為、アプリの機能がご利用いただけません。位置情報の設定をONに変更して下さい。")
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //位置情報の取得
    public void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false &&
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == false){
            //位置情報がオンになっているかの確認
            checkGpsSettings();
        }
        // Criteriaオブジェクトを生成
        Criteria criteria = new Criteria();
        //位置情報の精度
        criteria.setAccuracy(Criteria.NO_REQUIREMENT);
        //消費電力
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //ロケーションプロバイダの取得
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final String[] permissions = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 0);
            return;
        }
        if (locationManager.getLastKnownLocation(provider) != null){
            Location location = locationManager.getLastKnownLocation(provider);
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
            Position position = new Position(latitude, longitude);
            gnaviCtrl.execute(position);
        }else {
            locationManager.requestLocationUpdates(provider, 0, 0, this);
        }
    }

    //ListFragmentを再生成して絞り込み条件を反映する
    private void updateListFragment(ArrayList<ShopParameter> shopList){
        viewPager.setOffscreenPageLimit(2);
        pagerAdapter.destroyAllItem(viewPager);
        pagerAdapter.setShopList(shopList);
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void listCallback(ShopParameter shop, boolean flag) {
        //rouletteFragmentを取得し、店情報セット
        RoulettePage roulettePage = (RoulettePage)pagerAdapter.findFragmentByPosition(viewPager, 2);
        roulettePage.setShopParameter(shop, flag);
        //todo マップでも同様な処理をする(マップクラスでsetShopParameterと同義なメソッド作って)
        Map map = (Map)pagerAdapter.findFragmentByPosition(viewPager, 1);
        map.setShopParameter(shop, flag);
        roulettePage = null;
        map          = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        onDestroyLocation();
        //位置情報取得を破棄
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);
        // 検索ボタン配置
        MenuItem searchItem = menu.findItem(R.id.searchView);
        _searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // SettingButtonが押されたとき
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), RangeCategorySettings.class);
            startActivityForResult(intent, SETTING_ACTIVITY);
            return true;
        }
        // SearchButtonが押されたとき
        search();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == SETTING_ACTIVITY){
            if(resultCode == RESULT_OK){
                //Listを更新
                updateListFragment((ArrayList<ShopParameter>)data.getSerializableExtra("ShopList"));
            }
        }
    }

    // 検索バーの選択時
    public void search(){
        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void onDestroyLocation(){
        //位置情報取得を破棄
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDestroyLocation();
    }
}
