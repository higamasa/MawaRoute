package com.owner.kit.kies.androidkanazawa2015.list;

import android.graphics.Bitmap;

/**
 * Created by atsusuke on 2016/02/02.
 */
public class CustomData {
    private Bitmap imageData_;
    private String shopName;
    private String shopCategory;
    public void setImagaData(Bitmap image) {
        imageData_ = image;
    }
    public Bitmap getImageData() {
        return imageData_;
    }
    public void setShopNameData(String shopName) {
        this.shopName = shopName;
    }
    public String getShopNameData() {
        return shopName;
    }
    public void setShopCategoryData(String shopCategory) {
        this.shopCategory = shopCategory;
    }
    public String getShopCategoryData() {
        return shopCategory;
    }
}
