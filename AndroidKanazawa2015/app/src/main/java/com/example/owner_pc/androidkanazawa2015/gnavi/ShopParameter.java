package com.example.owner_pc.androidkanazawa2015.gnavi;

/**
 * Created by owner-PC on 2016/01/15.
 */
public class ShopParameter {

    private String shopName;
    private String shopCategory;
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
        return position.latitude;
    }
    public double getLongitude(){
        return position.longitude;
    }
}
