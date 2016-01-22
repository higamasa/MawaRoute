package com.example.owner_pc.androidkanazawa2015.gnavi;

import java.util.ArrayList;

/**
 * Created by owner-PC on 2016/01/15.
 */
public class ShopCtrl {

    private static String[][] category;
    private static ArrayList<ShopList> shopList = new ArrayList<ShopList>();

    public void setShopList(ArrayList<ShopList> shopList){
        this.shopList = shopList;
    }

    public void setCategory(String[][] category){
        this.category = category;
    }

    public String[] getCategory(int range){
        return this.category[range];
    }

    public ArrayList<ShopParameter> shopFiltering(int range,String category){
        ArrayList<ShopParameter> filterList = new ArrayList<ShopParameter>();
        int size = shopList.get(range).shop.size();
        for(int i = 0;i < size;i++){
            if(shopList.get(range).shop.get(i).getShopCategory().contains(category)){
                filterList.add(shopList.get(range).shop.get(i));
            }
        }
        return filterList;
    }
}
