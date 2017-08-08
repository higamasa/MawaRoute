package com.owner.kit.kies.androidkanazawa2015.gnavi;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by owner-PC on 2016/01/15.
 */
public class ShopCtrl {
    private static ArrayList<ShopParameter> shopList = new ArrayList<ShopParameter>();
    private static ArrayList<RangeList> rangeList = new ArrayList<RangeList>();

    //絞り込み条件を反映
    public void categoryDividing() {
        SettingParameter setting = new SettingParameter();
        //店リスト初期化
        shopList.clear();
        //範囲距離、カテゴリ分け
        int range = setting.getRangeType();
        if (setting.isFastFood())  shopList.addAll(rangeList.get(range).fastFood);
        if (setting.isCafe())      shopList.addAll(rangeList.get(range).cafe);
        if (setting.isHighCal())   shopList.addAll(rangeList.get(range).highCal);
        if (setting.isHighGrade()) shopList.addAll(rangeList.get(range).highGrade);
        if (setting.isWine())      shopList.addAll(rangeList.get(range).wine);
        if (setting.isOther())     shopList.addAll(rangeList.get(range).other);
        //nullの店を削除
        shopList.removeAll(Collections.singleton(null));
        //キーワード検索
        shopList = shopFiltering(setting.getKeyword());

    }

    public void setRangeList(ArrayList<RangeList> rangeList) {
        this.rangeList = rangeList;
    }

    public ArrayList<ShopParameter> getShopList() {
        return shopList;
    }

    //距離とカテゴリーキーワードを入力
    private ArrayList<ShopParameter> shopFiltering(String keyword) {
        if (keyword == null) {
            return shopList;
        }
        ArrayList<ShopParameter> filterList = new ArrayList<ShopParameter>();
        int size = shopList.size();
        for (int i = 0; i < size; i++) {
            if (shopList.get(i).getShopCategory().contains(keyword)
                    || shopList.get(i).getShopName().contains(keyword)) {
                filterList.add(shopList.get(i));
            }
        }
        return filterList;
    }
}
