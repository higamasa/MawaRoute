package com.example.owner_pc.androidkanazawa2015;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by owner-PC on 2016/02/02.
 */
public class RoulettePage extends Fragment implements Animation.AnimationListener{

    private Activity activity = new Activity();

    //家紋のViewGroup
    private FrameLayout frameLayout;

    //家紋の円
    private ImageView[] circle = new ImageView[CIR_NUM];

    //円の上に乗るアイコン
    private BitmapDrawable[] Icon = new BitmapDrawable[CIR_NUM];

    //家紋中の円の数、円のサイズ
    private static final int CIR_NUM = 5;
    private int cirSize;

    //回転の中心座標（家紋の中心）
    private Point center = new Point();

    //回転角度
    private int hitAngle;
    private int endAngle;

    //x軸最低スワイプ距離
    private static final int SWIPE_MIN_DISTANCE = 100;

    //x軸スワイプスピード
    private static final int SWIPE_THRESHOLD_VELOCITY = 1000;

    //y軸の移動距離　これ以上なら横移動を判定しない
    private static final int SWIPE_MAX_OFF_PATH = 600;

    //レンダリング後の情報取得リスナー
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.roulette_fragment, container, false);
        //ViewGroupのフレームレイアウトをセット
        frameLayout = (FrameLayout)view.findViewById(R.id.roulette_page);

        //スマホ画面の大きさから家紋のサイズを計算
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //円一つを画面サイズ（横）の2/7の大きさにする
        cirSize = size.x*2/7;

        //円のグラビティを中心に設定
        FrameLayout.LayoutParams[] params = new FrameLayout.LayoutParams[CIR_NUM];
        for(int i = 0; i < CIR_NUM; ++i){
            params[i] = new FrameLayout.LayoutParams(cirSize, cirSize, Gravity.CENTER);
        }
        //五角形の位置設定
        params[0].setMargins(0, 0, 0, 1 * cirSize);
        params[1].setMargins((int) (0.95 * cirSize), 0, 0, (int) (0.31 * cirSize));
        params[2].setMargins((int) (0.59 * cirSize), (int) (0.81 * cirSize), 0, 0);
        params[3].setMargins(0, (int) (0.81 * cirSize), (int) (0.59 * cirSize), 0);
        params[4].setMargins(0, 0, (int)(0.95*cirSize), (int)(0.31*cirSize));

        //IDの箱
        int[] circleId = new int[5];
        int[] categoryId = new int[5];
        //ID名
        String[] circleStr = {"cir_r", "cir_b", "cir_y", "cir_p", "cir_g"};
        String[] categoryStr = {"category1", "category2", "category2", "category2", "category2"};
        Drawable[][] drawables = new Drawable[5][2];
        LayerDrawable[] layerDrawable = new LayerDrawable[5];

        //それぞれの円のマージンを調整しFrameLayoutに追加
        for(int i=0; i < CIR_NUM; ++i){
            circle[i] = new ImageView(activity);
            //ID設定
            circleId[i] = getResources().getIdentifier(circleStr[i], "drawable", activity.getPackageName());
            categoryId[i] = getResources().getIdentifier(categoryStr[i], "drawable", activity.getPackageName());
            //BitmapDrawableにキャスト
            Icon[i] = (BitmapDrawable)getResources().getDrawable(categoryId[i]);
            //Bitmapファイルを取る
            Bitmap bmp = Icon[i].getBitmap();
            //回転(72度づつずらす）
            Matrix matrix = new Matrix();
            matrix.postRotate(72*i);
            //Bitmap回転
            Bitmap flippedBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, false);
            //加工後BitmapDrawableに戻す
            Icon[i] = new BitmapDrawable(flippedBmp);
            //円とアイコンを重ねる
            drawables[i][0] = getResources().getDrawable(circleId[i]);
            drawables[i][1] = Icon[i];
            //frameLayoutに表示
            layerDrawable[i] = new LayerDrawable(drawables[i]);
            circle[i].setImageDrawable(layerDrawable[i]);
            frameLayout.addView(circle[i], params[i]);
        }

        //レンダリング後
        //回転の中心座標を家紋の一番上の画像から割り出す
        globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                center.x = circle[0].getLeft()   + cirSize/2;
                center.y = circle[0].getBottom() + cirSize/2;

                // removeOnGlobalLayoutListener()の削除
                circle[0].getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
            }
        };
        circle[0].getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);

        //フリック判定で家紋を回転させる(回転処理はここに入っています）
        //タッチイベントのリスナー
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    //タッチ
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    //フリック
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
                                System.out.println("縦の移動距離が大きすぎ");
                                return false;
                            }
                            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                System.out.println("左から右");
                            } //ルーレットの右下フッリクしたら回転
                            else if(e1.getY() < e2.getY()
                                    && e1.getY() > center.y
                                    && e1.getY() < center.y + cirSize*2
                                    && e1.getX() > center.x - cirSize/2) {
                                System.out.println("右から左");
                                setRotate((int) (Math.abs(velocityX) / 1000));
                                startRotate();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        //roulette_layoutにOnTouchListener追加
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        return view;
    }

    //回転アニメーション初期化
    private void setRotate(int rotateTime){
        Random r = new Random();
        hitAngle = r.nextInt(360);
        //回転数制限
        if(rotateTime >= 8){
            rotateTime = 8;
        }else if(rotateTime <=4){
            rotateTime = 4;
        }
        //最終的な角度はフリックの強さ（回転数）+　36（0度始まりに調整）
        endAngle = hitAngle + 360*rotateTime + 36;
    }

    //回転アニメーション開始
    private void startRotate(){
        //回転アニメーションクラス生成
        RotateAnimation rotate = new RotateAnimation(0, endAngle, center.x, center.y);
        //3000msかけて回転
        rotate.setDuration(3000);
        //アニメーション後の状態保持（回った後そのまま）
        rotate.setFillAfter(true);
        rotate.setFillEnabled(true);
        rotate.setAnimationListener(this);
        //アニメーション開始
        frameLayout.startAnimation(rotate);
    }

    //アニメーション後
    @Override
    public void onAnimationEnd(Animation animation) {
        Toast.makeText(activity, "AnimationEnd", Toast.LENGTH_SHORT).show();
    }

    //アニメーション繰り返し
    @Override
    public void onAnimationRepeat(Animation animation) {
        Toast.makeText(activity, "AnimationRepeat", Toast.LENGTH_SHORT).show();
    }

    //アニメーション開始
    @Override
    public void onAnimationStart(Animation animation) {
        Toast.makeText(activity, "AnimationStart", Toast.LENGTH_SHORT).show();
    }

}
