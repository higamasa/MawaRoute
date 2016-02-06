package com.example.owner_pc.androidkanazawa2015;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.owner_pc.androidkanazawa2015.gnavi.AsyncTaskCallbacks;
import com.example.owner_pc.androidkanazawa2015.gnavi.GnaviCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.Position;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;

public class MainActivity extends AppCompatActivity implements AsyncTaskCallbacks, View.OnClickListener, ViewPager.OnPageChangeListener{

    private GnaviCtrl gnaviCtrl = new GnaviCtrl(this, this);
    private double latitude  = 36.594682;
    private double longitude = 136.625573;
    private ImageButton rightButton = null;
    private ImageButton leftButton  = null;


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

    //ぐるナビ読み込み完了
    @Override
    public void onTaskFinished(){
        //TabとSwipeの読み込み
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager()
                , MainActivity.this, latitude, longitude, new ShopCtrl()));
        viewPager.addOnPageChangeListener(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        //ボタンの表示
        onDrawButton();
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

    //画面下部のピンク色のボタン表示
    private void onDrawButton(){

        //スマホ画面サイズ取得
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //ボタンに画像をセット
        rightButton = (ImageButton)findViewById(R.id.right_button);
        leftButton = (ImageButton)findViewById(R.id.left_button);
        rightButton.setImageResource(R.drawable.under_button_p_r);
        leftButton.setImageResource(R.drawable.under_button_p);

        //ボタンの位置、グラビティ...etc取得
        ViewGroup.LayoutParams[] params = new ViewGroup.LayoutParams[2];
        params[0] = rightButton.getLayoutParams();
        params[1] = leftButton.getLayoutParams();

        //ボタンサイズ変更
        for(int i = 0; i < 2; ++i){
            params[i].width  = size.x / 3;
            params[i].height = size.y / 3;
        }

        //サイズ変更を反映
        rightButton.setLayoutParams(params[0]);
        leftButton.setLayoutParams(params[1]);

        //クリック可能
        rightButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);

    }

    //ボタンが押されたらポップアップ表示
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.right_button:
                Log.d("MainActivity", "pushed right");
                //todo ポップアップ（距離、カテゴリ）
                //todo listに条件絞り込みを渡す
                break;
            case R.id.left_button:
                Log.d("MainActivity", "pushed left");
                //todo ポップアップ（キーワード検索）
                //todo listに条件絞り込みを渡す
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.d("MainActivity", "onPageScrolled() position="+position);
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("MainActivity", "onPageSelected() position="+position);
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
