package com.example.owner_pc.androidkanazawa2015.gnavi;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by owner-PC on 2016/01/15.
 */
public class ShopCtrl extends AppCompatActivity implements Serializable {

    private static String[][] category;
    private static ArrayList<ShopList> shopList = new ArrayList<ShopList>();
    private static ArrayList<ShopParameter> shopList2 = new ArrayList<ShopParameter>();
    private static ArrayList<RangeList> rangeList = new ArrayList<RangeList>();

    public void categoryDividing(){
        SettingParameter setting = new SettingParameter();
        int range = setting.getRangeType();
        if(setting.isFastFood())  shopList2.addAll(rangeList.get(range).fastFood);
        if(setting.isCafe())      shopList2.addAll(rangeList.get(range).cafe);
        if(setting.isHighCal())   shopList2.addAll(rangeList.get(range).highCal);
        if(setting.isHighGrade()) shopList2.addAll(rangeList.get(range).highGrade);
        if(setting.isWine())      shopList2.addAll(rangeList.get(range).wine);
        if(setting.isOther())     shopList2.addAll(rangeList.get(range).other);
        shopList2 = shopFiltering(setting.getKeyword());
    }

    public void setRangeList(ArrayList<RangeList> rangeList){
        this.rangeList = rangeList;
    }
    public ArrayList<RangeList> getRangeList(){
        return rangeList;
    }

    public void setShopList(ArrayList<ShopList> shopList){
        this.shopList = shopList;
    }
    public ArrayList<ShopList> getShopList(){
        return shopList;
    }
    //todo getShopList->getShopList2
    public ArrayList<ShopParameter> getShopList2(){
        return shopList2;
    }

    public void setCategory(String[][] category){
        this.category = category;
    }
    public String[] getCategory(int range){
        return this.category[range];
    }

    //距離とカテゴリーキーワードを入力
    private ArrayList<ShopParameter> shopFiltering(String keyword){
        if(keyword == null){
            return shopList2;
        }
        ArrayList<ShopParameter> filterList = new ArrayList<ShopParameter>();
        int size = shopList2.size();
        for(int i = 0;i < size;i++){
            if(shopList2.get(i).getShopCategory().contains(keyword)
                    || shopList2.get(i).getShopName().contains(keyword)){
                filterList.add(shopList2.get(i));
            }
        }
        return filterList;
    }
}
