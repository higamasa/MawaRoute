package com.example.owner_pc.androidkanazawa2015;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by owner-PC on 2016/02/06.
 */
public class SettingButton implements View.OnClickListener{

    private ImageButton rightButton = null;
    private ImageButton leftButton  = null;
    private Activity activity = null;

    public SettingButton (Activity activity){
        this.activity = activity;
    }

    public ImageButton getRightButton(){
        return this.rightButton;
    }

    public ImageButton getLeftButton(){
        return this.leftButton;
    }

    //画面下部のピンク色のボタン表示
    public void onDrawButton(){

        //スマホ画面サイズ取得
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //ボタンに画像をセット
        rightButton = (ImageButton)activity.findViewById(R.id.right_button);
        leftButton = (ImageButton)activity.findViewById(R.id.left_button);
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

}
