package kies.mawaroute.gnavi;

import java.io.Serializable;

/**
 * Created by owner-PC on 2016/01/15.
 */
public class ShopParameter implements Serializable {

    private String shopName;
    private String shopCategory;
    private String shopCategoryType;
    private String shopUrl;
    private String openTime;
    private String shopAddress;
    private String shopImage;
    private Position position = new Position();

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String name) {
        shopName = name;
    }

    public String getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(String category) {
        shopCategory = category;
    }

    public String getShopCategoryType() {
        return shopCategoryType;
    }

    public void setShopCategoryType(String categoryType) {
        shopCategoryType = categoryType;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String url) {
        shopUrl = url;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String time) {
        openTime = time;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String address) {
        shopAddress = address;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String image) {
        shopImage = image;
    }

    public double getLatitude() {
        return position.latitude - 0.00010695 * position.latitude + 0.000017464 * position.longitude + 0.0046017;
    }

    public void setLatitude(String latitude) {
        position.latitude = Double.parseDouble(latitude);
    }

    public double getLongitude() {
        return position.longitude - 0.000046038 * position.latitude - 0.000083043 * position.longitude + 0.010040;
    }

    public void setLongitude(String longitude) {
        position.longitude = Double.parseDouble(longitude);
    }
}
