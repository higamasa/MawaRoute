package com.example.owner_pc.androidkanazawa2015.gnavi;

import java.io.Serializable;

/**
 * Created by owner-PC on 2016/01/15.
 */
public class ShopParameter implements Serializable{

    private String shopName;
    private String shopCategory;
    private String shopCategoryType;
    private String shopUrl;
    private String openTime;
    private String shopAddress;
    private String shopImage;
    private Position position = new Position();

    public void setShopName(String name){
        shopName = name;
    }
    public void setShopCategory(String category){
        shopCategory = category;
    }
    public void setShopCategoryType(String categoryType){
        shopCategoryType = categoryType;
    }
    public void setShopUrl(String url){
        shopUrl = url;
    }
    public void setOpenTime(String time){
        openTime = time;
    }
    public void setShopAddress(String address){
        shopAddress = address;
    }
    public void setShopImage(String image){
        shopImage = image;
    }
    public void setLatitude(String latitude){
        position.latitude = Double.parseDouble(latitude);
    }
    public void setLongitude(String longitude){
        position.longitude = Double.parseDouble(longitude);
    }

    public String getShopName(){
        return shopName;
    }
    public String getShopCategory(){
        return shopCategory;
    }
    public String getShopCategoryType(){
        return shopCategoryType;
    }
    public String getShopUrl(){
        return shopUrl;
    }
    public String getOpenTime(){
        return openTime;
    }
    public String getShopAddress(){
        return shopAddress;
    }
    public String getShopImage(){
        return shopImage;
    }
    public double getLatitude(){
        return position.latitude - 0.00010695*position.latitude  + 0.000017464*position.longitude + 0.0046017;
    }
    public double getLongitude(){
        return position.longitude - 0.000046038*position.latitude - 0.000083043*position.longitude + 0.010040;
    }
}
