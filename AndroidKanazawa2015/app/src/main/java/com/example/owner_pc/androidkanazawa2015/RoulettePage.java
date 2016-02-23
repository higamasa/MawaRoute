package com.example.owner_pc.androidkanazawa2015;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner_pc.androidkanazawa2015.gnavi.Position;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopParameter;
import com.example.owner_pc.androidkanazawa2015.google_map.RouteShop;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by owner-PC on 2016/02/02.
 */
public class RoulettePage extends Fragment{

    private View view;
    private RouteShop routeShop;
    PopupWindow mPopupWindow;

    TranslateAnimation translate;

//    private Activity activity;

    //家紋ごとの店の情報リスト
    private ArrayList<ShopParameter> shopList = new ArrayList<ShopParameter>();

    private Position start = new Position();

    //回転後の店番号
    private int hitNum;

    //家紋のViewGroup
    private FrameLayout frameLayout;
    private FrameLayout kamonLayout;
    private FrameLayout yumiyaLayout;
    private FrameLayout signLayout;

    //矢印
    private ImageView sign;

    //弓矢
    private ImageView bow;
    private ImageView arrow;

    //家紋の円
    private ImageView[] circle = new ImageView[CIR_NUM + 1];

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
        routeShop = new RouteShop(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.roulette_fragment, container, false);

        //スマホ画面の大きさから家紋のサイズを計算
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //円一つを画面サイズ（横）の2/7の大きさにする
        cirSize = size.x*2/7;

        //ViewGroupのフレームレイアウトをセット
        frameLayout  = (FrameLayout)view.findViewById(R.id.roulette_page);
        kamonLayout  = (FrameLayout)view.findViewById(R.id.kamon);
        yumiyaLayout = (FrameLayout)view.findViewById(R.id.yumiya);
        signLayout   = (FrameLayout)view.findViewById(R.id.sign);
        kamonLayout.setPadding(0, cirSize/2, 0, 0);

        //矢印のグラビティ設定
        FrameLayout.LayoutParams signParam  = new FrameLayout.LayoutParams(cirSize*2,cirSize*2, Gravity.RIGHT|Gravity.BOTTOM);

        //弓矢のグラビティを設定
        FrameLayout.LayoutParams bowParam   = new FrameLayout.LayoutParams(cirSize, cirSize, Gravity.CENTER);
        FrameLayout.LayoutParams arrowParam = new FrameLayout.LayoutParams(cirSize, cirSize, Gravity.CENTER);

        //円のグラビティを中心に設定
        FrameLayout.LayoutParams[] params = new FrameLayout.LayoutParams[CIR_NUM + 1];
        for(int i = 0; i < CIR_NUM; ++i){
            params[i] = new FrameLayout.LayoutParams(cirSize, cirSize, Gravity.CENTER);
        }
        cirSize = cirSize - (cirSize/8);
        //弓矢の位置設定
        bowParam.setMargins(0, 0, 0, 2 * cirSize);
        arrowParam.setMargins(0, 0, 0, 2 * cirSize);
        //五角形の位置設定
        params[0].setMargins(0, 0, 0, 1 * cirSize);
        params[1].setMargins((int) (0.95 * cirSize), 0, 0, (int) (0.31 * cirSize));
        params[2].setMargins((int) (0.59 * cirSize), (int) (0.81 * cirSize), 0, 0);
        params[3].setMargins(0, (int) (0.81 * cirSize), (int) (0.59 * cirSize), 0);
        params[4].setMargins(0, 0, (int) (0.95 * cirSize), (int) (0.31 * cirSize));
        params[5] = new FrameLayout.LayoutParams(cirSize*9/12, cirSize*9/12, Gravity.CENTER);

        //弓矢
        bow   = new ImageView(getActivity());
        arrow = new ImageView(getActivity());
        bow.setImageResource(R.drawable.bow);
        arrow.setImageResource(R.drawable.arrow);
        yumiyaLayout.addView(bow, bowParam);
        yumiyaLayout.addView(arrow, arrowParam);

        //矢印
        sign = new ImageView(getActivity());
        sign.setImageResource(R.drawable.yazirushi2);
        signLayout.addView(sign, signParam);

        //画像ID（家紋、カテゴリ）
        int[] circleId = new int[CIR_NUM + 1];
        int[] categoryId = new int[CIR_NUM];
        //ID名
        String[] circleStr = {"cir_r", "cir_b", "cir_y", "cir_p", "cir_g", "cir_gray"};
        //todo カテゴリを店情報から決める
        String[] categoryStr = {"category1", "category2", "category2", "category2", "category2"};
//        String[] categoryStr = setCategoryID();
        //円とカテゴリマークを重ねる変数
        Drawable[][] drawables = new Drawable[5][2];
        LayerDrawable[] layerDrawable = new LayerDrawable[5];

        //家紋の位置、向きを調整しFrameLayoutに追加する
        for(int i=0; i < CIR_NUM; ++i){
            circle[i] = new ImageView(getActivity());
            //ID設定
            circleId[i] = getResources().getIdentifier(circleStr[i], "drawable", getActivity().getPackageName());
            categoryId[i] = getResources().getIdentifier(categoryStr[i], "drawable", getActivity().getPackageName());
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
            kamonLayout.addView(circle[i], params[i]);

            //解放
            bmp = null;
            flippedBmp = null;
            drawables[i][0] = null;
            drawables[i][1] = null;
            layerDrawable[i] = null;
        }

        //真ん中の円を表示
        circle[5] = new ImageView(getActivity());
        circle[5].setImageResource(R.drawable.cir_gray);
        kamonLayout.addView(circle[5], params[5]);

        //レンダリング後
        //回転の中心座標を家紋の一番上の画像から割り出す
        globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                center.x = circle[5].getLeft() + circle[5].getWidth()/2;
                center.y = circle[5].getTop() + circle[5].getHeight()/2;

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
                            } //ルーレットの右下フリックしたら回転
                            else if(e1.getY() < e2.getY()
                                    && e1.getY() > center.y
                                    && e1.getY() < center.y + cirSize*2
                                    && e1.getX() > center.x - cirSize/2) {
                                System.out.println("右から左");
                                //todo toast表示
                                if(shopList.size() != 0) {
                                    setRotate((int) (Math.abs(velocityX) / 1000));
                                    startRotate();
                                }else{
                                    Toast.makeText(getActivity(), "店を一個以上選択してください", Toast.LENGTH_SHORT).show();
                                }
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

    private String[] setCategoryID(){
        //カテゴリ画像のID
        String[] categoryID = new String[5];
        //ショップがあるならカテゴリ画像を、無いならナシ画像を入れる
        for(int i = 0; i < 5; ++i){
            if(shopList.get(i) != null){
                categoryID[i] = shopList.get(i).getShopCategoryType();
            }else{
                //todo noItem画像追加
                categoryID[i] = "noItem";
            }
        }

        return categoryID;
    }

    private int Hit(){
        switch (hitAngle / 72) {
            case 0:
                System.out.println("5番");
                System.out.println(hitAngle);
                return 4;
            case 1:
                System.out.println("4番");
                System.out.println(hitAngle);
                return 3;
            case 2:
                System.out.println("3番");
                System.out.println(hitAngle);
                return 2;
            case 3:
                System.out.println("2番");
                System.out.println(hitAngle);
                return 1;
            case 4:
                System.out.println("1番");
                System.out.println(hitAngle);
                return 0;
            default:
                return 0;
        }
    }

    //弓矢を戻す(上昇)
    private void backTranslate(){
        translate = new TranslateAnimation(0, 0, 0, 0);
        translate.setDuration(500);
        translate.setFillAfter(true);
        arrow.startAnimation(translate);
    }

    //弓矢を放つ(下降)
    private void startTranslate(){
        translate = new TranslateAnimation(0, 0, 0, cirSize);
        translate.setDuration(500);
        translate.setFillAfter(true);
        arrow.startAnimation(translate);
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //弓矢で刺した家紋のお店情報をポップアップで表示
            @Override
            public void onAnimationEnd(Animation animation) {
                // ポップアップ作成
                final PopupWindow mPopupWindow = new PopupWindow(
                        null,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View popupView = layoutInflater.inflate(R.layout.popup_layout, null);

                TextView popupText;

                popupText = (TextView)popupView.findViewById(R.id.popupText);
                popupText.setText(shopList.get(hitNum).getShopName());

                // 閉じるボタンを押したとき
                popupView.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ポップアップが表示されている場合
                        if (mPopupWindow.isShowing()) {
                            // ポップアップ破棄
                            //弓矢戻す
                            backTranslate();
                            mPopupWindow.dismiss();
                        }
                    }
                });

                // OKボタンを押したとき
                popupView.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ポップアップが表示されている場合
                        //
                        if (mPopupWindow.isShowing()) {
                            Position goal = new Position(shopList.get(hitNum).getLatitude(), shopList.get(hitNum).getLongitude());
                            routeShop.Route(start, goal);
                            //弓矢戻す
                            backTranslate();
                            mPopupWindow.dismiss();
                        }
                    }
                });

                // 詳細ボタンを押したとき
                popupView.findViewById(R.id.detail_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // ポップアップが表示されている場合
                        if (mPopupWindow.isShowing()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(shopList.get(hitNum).getShopUrl()));
                            startActivity(intent);
                            //弓矢戻す
                            backTranslate();
                        }
                    }
                });

                mPopupWindow.setContentView(popupView);

                // 背景設定
                mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));

                // タップ時に他のViewでもキャッチするための設定
                mPopupWindow.setOutsideTouchable(false);
                mPopupWindow.setFocusable(false);

                // 表示サイズの設定
                //float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
                mPopupWindow.setWindowLayoutMode(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

                // アニメーションスタイル配置
                mPopupWindow.setAnimationStyle(R.style.Animation);

                // 画面表示
                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //回転アニメーション初期化
    private void setRotate(int rotateTime){
        Random r = new Random();
        //todo 格納されている店の個数で回転角度を決める
        int shopNum = (CIR_NUM - shopList.size());
        hitAngle = r.nextInt(72*shopList.size()) + 72 * shopNum;
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
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            //回転後弓矢を放つ
            @Override
            public void onAnimationEnd(Animation animation) {
                startTranslate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //アニメーション開始
        kamonLayout.startAnimation(rotate);
        //店の番号
        hitNum = Hit();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        //Listener解除
        circle[0].getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        globalLayoutListener = null;
        //矢印
        sign.setImageDrawable(null);
        //弓矢
        bow.setImageDrawable(null);
        arrow.setImageDrawable(null);

        //家紋の円
        for(int i = 0; i < CIR_NUM; ++i){
            circle[i].setImageDrawable(null);
            Icon[i] = null;
        }
        circle[CIR_NUM].setImageDrawable(null);

        //layout解放
        frameLayout  = null;
        kamonLayout  = null;
        yumiyaLayout = null;
        signLayout   = null;

        //ルーレットview削除
        view = null;
    }

    @Override
    public void onDestroy(){
        // ポップアップが表示されている場合
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            // ポップアップを閉じる
            mPopupWindow.dismiss();
        }
        //

        super.onDestroy();
    }

    private void editShopList(ShopParameter shop, boolean flag){
        if(flag){
            //リストに同じ店がないので追加する
            shopList.add(shop);
        }else{
            //リストに同じ店があるので排除する
            for(int i = 0; i < shopList.size(); ++i){
                String shopName = shopList.get(i).getShopName();
                if(shopName.equals(shop.getShopName())){
                    shopList.remove(i);
                    return;
                }
            }
        }
    }

    //MainActivityから選択された店の情報を受け取り、追加判定をする
    public void setShopParameter(ShopParameter shop, boolean flag){
        editShopList(shop, flag);
    }

    public void setPosition(Position position){
        this.start = position;
    }



}
