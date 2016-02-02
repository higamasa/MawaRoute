package com.example.owner_pc.androidkanazawa2015.list;

import android.graphics.Bitmap;

/**
 * Created by atsusuke on 2016/02/02.
 */
public class CustomData {
    private Bitmap imageData_;
    private String textData_;
    public void setImagaData(Bitmap image) {
        imageData_ = image;
    }
    public Bitmap getImageData() {
        return imageData_;
    }
    public void setTextData(String text) {
        textData_ = text;
    }
    public String getTextData() {
        return textData_;
    }
}
