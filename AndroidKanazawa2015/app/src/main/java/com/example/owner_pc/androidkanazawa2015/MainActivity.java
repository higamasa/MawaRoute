package com.example.owner_pc.androidkanazawa2015;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
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
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.example.owner_pc.androidkanazawa2015.gnavi.AsyncTaskCallbacks;
import com.example.owner_pc.androidkanazawa2015.gnavi.GnaviCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.Position;
import com.example.owner_pc.androidkanazawa2015.gnavi.SettingParameter;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopParameter;
import com.example.owner_pc.androidkanazawa2015.google_map.Map;
import com.example.owner_pc.androidkanazawa2015.list.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AsyncTaskCallbacks,List.FragmentTopCallback,LocationListener, SearchView.OnQueryTextListener {

    private boolean popupDismissFlag = false;
    private PopupWindow splashPopup;
    private ImageView kamon;
    private ImageView tudumi;
    private ImageView sign;
    private Toolbar _toolBar;
    private SearchView _searchView;
    private MainFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private GnaviCtrl gnaviCtrl = new GnaviCtrl(this, this);
    private LocationManager locationManager = null;
    private ShopCtrl _shopCtrl = new ShopCtrl();
    private SettingParameter _settingParam = new SettingParameter();
    private String rangeNum;
    private double latitude;
    private double longitude;
    private Position position = new Position();
    private static final int SETTING_ACTIVITY = 1000;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //タイトルロゴ表示
        showSplashWindow();
        setContentView(R.layout.activity_main);
        // ツールバー配置
        _toolBar = (Toolbar)findViewById(R.id.tool_bar);
        //_toolBar.setTitle("お店一覧");
        setSupportActionBar(_toolBar);
    }

    @Override
    public void onResume(){
        super.onResume();
        //位置情報の読み込み
        getLocation();
    }

    private void showSplashWindow(){
        //スプラッシュロゴ表示
        splashPopup = new PopupWindow(MainActivity.this);
        final View splashView = getLayoutInflater().inflate(R.layout.splash_popup, null);
        splashPopup.setContentView(splashView);

        //スマホ画面の大きさからロゴのサイズを計算
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        final int kamonSize = size.x/2;

        // 背景設定
        splashPopup.setBackgroundDrawable(getResources().getDrawable(R.drawable.splash_background));

        // タップ時に他のViewでキャッチされないための設定
        splashPopup.setOutsideTouchable(true);
        splashPopup.setFocusable(true);

        // 表示サイズの設定
        splashPopup.setWindowLayoutMode(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        splashPopup.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        splashPopup.setHeight(WindowManager.LayoutParams.FILL_PARENT);

        // 画面中央に表示
        splashView.post(new Runnable() {
            @Override
            public void run() {
                splashPopup.showAtLocation(splashView, Gravity.CENTER, 0, 0);

                FrameLayout.LayoutParams kamonParam = new FrameLayout.LayoutParams(kamonSize, kamonSize, Gravity.CENTER);

                FrameLayout frameLayout = (FrameLayout) splashView.findViewById(R.id.kamon_icon);
                kamon = new ImageView(getApplicationContext());
                tudumi = new ImageView(getApplicationContext());
                sign = new ImageView(getApplicationContext());
                kamon.setAlpha(0.0f);
                tudumi.setAlpha(0.0f);
                sign.setAlpha(0.0f);

                kamon.setImageResource(R.drawable.kamon_kamon);
                tudumi.setImageResource(R.drawable.kamon_tudumi);
                sign.setImageResource(R.drawable.kamon_yazirushi);

                frameLayout.addView(tudumi, kamonParam);
                frameLayout.addView(kamon, kamonParam);
                frameLayout.addView(sign, kamonParam);

                //ロゴアニメーション開始
                tudumiAnimation();

            }
        });
    }

    //鼓門フェードイン
    private void tudumiAnimation() {
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);
        alpha.setFillAfter(true);
        tudumi.setAlpha(1.0f);
        tudumi.startAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            //家紋フェードインへ
            @Override
            public void onAnimationEnd(Animation animation) {
                kamonAlphaAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    //家紋フェードイン
    private void kamonAlphaAnimation() {
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);
        alpha.setFillAfter(true);
        kamon.setAlpha(1.0f);
        kamon.startAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            //矢印フェードインへ
            @Override
            public void onAnimationEnd(Animation animation) {
                signAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    //家紋回転
    private void kamonRotateAnimation() {
        RotateAnimation rotate = new RotateAnimation(0, 360, kamon.getWidth()/2 + 3, kamon.getHeight()/2 + 13);
        rotate.setDuration(750);
        rotate.setFillAfter(false);
        kamon.startAnimation(rotate);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //ロードが終わっていたらポップアップを消す
                if(popupDismissFlag){
                    splashPopup.dismiss();
                }
                //ロードが終わっていなければポップアップ消去フラグをONにする。
                else{
                    popupDismissFlag = true;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    //矢印フェードイン
    private void signAnimation() {
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(500);
        alpha.setFillAfter(true);
        sign.setAlpha(1.0f);
        sign.startAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            //家紋回転へ
            @Override
            public void onAnimationEnd(Animation animation) {
                kamonRotateAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    //ぐるナビ読み込み完了
    @Override
    public void onTaskFinished(){
        //ツールバータイトル設定
        switch (_settingParam.getRangeType()){
            case 0:
                rangeNum = "300m";
                break;
            case 1:
                rangeNum = "500m";
                break;
            case 2:
                rangeNum = "1000m";
                break;
        }
        _toolBar.setTitle(rangeNum + "圏内のお店");
        //TabとSwipeの読み込み
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        _toolBar.setTitle(rangeNum + "圏内のお店");
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
        tabLayout.getTabAt(2).setIcon(R.drawable.rulette_tab);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //現在位置をルーレットページにセット
        RoulettePage roulettePage = (RoulettePage)pagerAdapter.findFragmentByPosition(viewPager, 2);
        roulettePage.setPosition(position);
        roulettePage = null;

        //ロゴアニメーションが終わっていたらポップアップを消す
        if(popupDismissFlag){
            splashPopup.dismiss();
        }
        //アニメーションが終わっていなければポップアップ消去フラグをONにする。
        else{
            popupDismissFlag = true;
        }

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
        //位置情報がオンになっているかの確認
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false &&
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == false) {
            //位置情報がオンになっているかの確認
            checkGpsSettings();
        } else {
            // Criteriaオブジェクトを生成
            Criteria criteria = new Criteria();
            //方位不要
            criteria.setBearingRequired(false);
            // 速度不要
            criteria.setSpeedRequired(false);
            // 高度不要
            criteria.setAltitudeRequired(false);
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
                onGnaviSetting(location.getLatitude(), location.getLongitude());
            }else {
                locationManager.requestLocationUpdates(provider, 0, 0, this);
            }
        }
    }

    private void onGnaviSetting(double latitude , double longitude){
        if (flag == true) {
            this.latitude = latitude;
            this.longitude = longitude;
            position.latitude = latitude;
            position.longitude = longitude;
            gnaviCtrl.execute(position);
            this.flag = false;
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

        //ツールバータイトル設定
        switch (_settingParam.getRangeType()){
            case 0:
                rangeNum = "300m";
                break;
            case 1:
                rangeNum = "500m";
                break;
            case 2:
                rangeNum = "1000m";
                break;
        }
        _toolBar.setTitle(rangeNum + "圏内のお店");

        RoulettePage roulettePage = (RoulettePage)pagerAdapter.findFragmentByPosition(viewPager, 2);
        roulettePage.setPosition(position);
        roulettePage = null;
    }

    @Override
    public void listCallback(ShopParameter shop, boolean flag) {
        //rouletteFragmentを取得し、店情報セット
        RoulettePage roulettePage = (RoulettePage)pagerAdapter.findFragmentByPosition(viewPager, 2);
        roulettePage.setShopParameter(shop, flag);
        Map map = (Map)pagerAdapter.findFragmentByPosition(viewPager, 1);
        map.setShopParameter(shop, flag);
        roulettePage = null;
        map          = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        onGnaviSetting(location.getLatitude(),location.getLongitude());
        onDestroyLocation();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // 検索ボタン配置
        MenuItem searchItem = menu.findItem(R.id.searchView);
        // 検索ボタン画像差し替え
        _searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView)_searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.ic_menu_search_g);

        _searchView.setQueryHint("キーワードを入力してください");
        ((EditText)_searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.common_signin_btn_light_text_default));
        ((EditText)_searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setTextColor(getResources().getColor(R.color.colorGold));
        _searchView.setIconifiedByDefault(true);
        _searchView.setOnQueryTextListener(this);
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

    @Override
    public boolean onQueryTextSubmit(String searchWord ){
        _settingParam.setKeyword(searchWord);
        _shopCtrl.categoryDividing();
        updateListFragment(_shopCtrl.getShopList());
        _searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String searchWord){
        return true;
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
