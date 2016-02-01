package com.example.owner_pc.androidkanazawa2015;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.owner_pc.androidkanazawa2015.gnavi.GnaviCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.Position;

public class MainActivity extends AppCompatActivity {
    private PopupWindow mPopupWindow;
    GnaviCtrl gnaviCtrl = new GnaviCtrl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TabとSwipeの読み込み
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        //todo 現在地を入れてください
        Position position = new Position(36.594682, 136.625573);
        //ぐるナビの読み込み
        gnaviCtrl.execute(position);
    }

    // ポップアップの表示
    public void ShowPopupWindow() {
        mPopupWindow = new PopupWindow();

        // レイアウト設定
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);

        // 閉じるボタン
        popupView.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ポップアップがされている場合
                if (mPopupWindow.isShowing()) {
                    // ポップアップ破棄
                    // todo ここに処理
                    mPopupWindow.dismiss();
                }
            }
        });

        // OKボタンを押したとき
        popupView.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ポップアップが表示されている場合
                if (mPopupWindow.isShowing()) {
                    // ポップアップ破棄
                    // todo ここに処理
                    mPopupWindow.dismiss();
                }
            }
        });

        // 詳細ボタンを押したとき
        popupView.findViewById(R.id.detail_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // ポップアップが表示されている場合
                if(mPopupWindow.isShowing()){
                    // ポップアップ破棄
                    // todo ここに処理
                    mPopupWindow.dismiss();
                }
            }
        });

        mPopupWindow.setContentView(popupView);

        // 背景設定
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));

        // タップ時に他のViewでキャッチされないための設定
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // 表示サイズの設定 今回は仮に幅300dp
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        mPopupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth((int) width);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // 画面表示 todo どこを基準に表示するかを指定
        mPopupWindow.showAtLocation(findViewById(R.id.ok_button), Gravity.CENTER, 0, 0);
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
    public void onDestroy(){
        // ポップアップが表示されている場合
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            // ポップアップを閉じる
            mPopupWindow.dismiss();
        }
        super.onDestroy();
    }
}
