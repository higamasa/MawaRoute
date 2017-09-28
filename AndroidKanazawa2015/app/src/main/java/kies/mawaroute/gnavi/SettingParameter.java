package kies.mawaroute.gnavi;

/**
 * Created by owner-PC on 2016/02/10.
 */
public class SettingParameter {

    private static boolean fastFood;   //軽食
    private static boolean cafe;       //休憩
    private static boolean highCal;    //がっつり
    private static boolean highGrade;  //高級
    private static boolean wine;       //酒
    private static boolean other;      //その他

    private static int rangeType;      //距離(0～2)

    private static String keyword;     //検索ワード

    public void initSetting() {
        fastFood = true;
        cafe = true;
        highCal = true;
        highGrade = true;
        wine = true;
        other = true;
        rangeType = 0;
        keyword = null;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getRangeType() {
        return rangeType;
    }

    public void setRangeType(int rangeType) {
        this.rangeType = rangeType;
    }

    public boolean isFastFood() {
        return fastFood;
    }

    public void setFastFood(boolean flag) {
        fastFood = flag;
    }

    public boolean isCafe() {
        return cafe;
    }

    public void setCafe(boolean flag) {
        cafe = flag;
    }

    public boolean isHighCal() {
        return highCal;
    }

    public void setHighCal(boolean flag) {
        highCal = flag;
    }

    public boolean isHighGrade() {
        return highGrade;
    }

    public void setHighGrade(boolean flag) {
        highGrade = flag;
    }

    public boolean isWine() {
        return wine;
    }

    public void setWine(boolean flag) {
        wine = flag;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean flag) {
        other = flag;
    }

}
