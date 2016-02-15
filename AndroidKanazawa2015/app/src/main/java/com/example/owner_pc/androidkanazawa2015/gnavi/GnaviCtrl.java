package com.example.owner_pc.androidkanazawa2015.gnavi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by owner-PC on 2016/01/15.
 */
public class GnaviCtrl extends AsyncTask<Position,Void,String[]> {

    //コールバックインターフェース
    private AsyncTaskCallbacks callback = null;

    //プログレスダイアログ表示に必要
    private Activity activity = null;
    public ProgressDialog progressDialog;
    //距離ごとのリスト（5種類）、それぞれの中に店のリスト（複数）がある
    private ArrayList<ShopList> shopList = new ArrayList<ShopList>();
    //5種類の距離(300m, 500m, 1000m, 2000m, 3000m)
    //3種類の距離(300m, 500m, 1000m)
    private static final int RANGE = 3;

    public GnaviCtrl(Activity activity, AsyncTaskCallbacks callback){
        this.activity = activity;
        this.callback = callback;
        for(int i=0;i<RANGE;i++) {
            shopList.add(new ShopList());
        }
    }

    @Override
    protected void onPreExecute() {

        // プログレスダイアログの生成
        this.progressDialog = new ProgressDialog(this.activity);

        // プログレスダイアログの設定
        this.progressDialog.setMessage("読み込み中...");  // メッセージをセット

        // プログレスダイアログの表示
        this.progressDialog.show();

        return;
    }


    @Override
    protected String[] doInBackground(Position... positions) {

        URL urls[]        = new URL[RANGE];
        String[] result   = new String[RANGE];
        Position position = positions[0];
        HttpURLConnection urlConnection = null;

        for(int i=0;i<RANGE;i++){
            try {
                urls[i] = new URL("http://api.gnavi.co.jp/RestSearchAPI/20150630/?format=xml&keyid=6ba07fbbdb9a380e560240a84d2da924&latitude=" + position.latitude + "&longitude=" + position.longitude + "&range="+(i+1)+"&hit_per_page="+500);
                urlConnection = (HttpURLConnection) urls[i].openConnection();
                result[i] = InputStreamToString(urlConnection.getInputStream());
            }catch (Exception e){
                Log.d("XmlPullParserSampleUrl", "Error");
            }finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String[] response) {

        //xmlデータをshopListに書き込む
        xmlWriteInList(response);

        //店クラスに店リストとカテゴリ分けした文字配列をセット
        ArrayList<RangeList> rangeList = new ArrayList<RangeList>();
        ShopCtrl shopCtrl = new ShopCtrl();
        SettingParameter setting = new SettingParameter();


        //todo setShopListコメントアウトする
        shopCtrl.setShopList(shopList);
        //絞り込み条件を初期設定にする
        setting.initSetting();
        //ショップリストをカテゴリタイプ別に分ける
        rangeList = shopListDividing();
        shopCtrl.setRangeList(rangeList);
        //絞り込み条件のもとショップリスト生成
        shopCtrl.categoryDividing();

//        ArrayList<ShopParameter> shopPram = shopCtrl.getShopList2();
//
//        for(int i=0;i<shopPram.size();i++){
//            System.out.println(shopPram.get(i).getShopName());
//        }
//        for(int i=0;i<rangeList.get(0).fastFood.size();i++){
//            System.out.println(rangeList.get(0).fastFood.get(i).getShopName());
//        }

        // プログレスダイアログを閉じる
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }

        //終了をActivityに通知
        callback.onTaskFinished();
    }

    @Override
    protected void onCancelled(){
        Log.v("AsyncTask", "onCancelled");
        callback.onTaskCancelled();
    }

    public void xmlWriteInList(String[] xmlData) {

        XmlPullParser xmlPullParser = Xml.newPullParser();
        try {
            for (int i = 0; i < RANGE; i++) {
                xmlPullParser.setInput(new StringReader(xmlData[i]));

                int eventType;
                ShopParameter tmpShop = null;

                while ((eventType = xmlPullParser.next()) != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String xmlText = xmlPullParser.getName();
                        if (xmlText == null) {
                            Log.d("XmlPullParserSampleUrl", "Text is null");
                        } else switch (xmlText) {
                            case "name":
                                tmpShop = new ShopParameter();
                                tmpShop.setShopName(xmlPullParser.nextText());
                                //新しい店追加
                                shopList.get(i).shop.add(new ShopParameter());
                                break;
                            case "latitude":
                                tmpShop.setLatitude(xmlPullParser.nextText());
                                break;
                            case "longitude":
                                tmpShop.setLongitude(xmlPullParser.nextText());
                                break;
                            case "category":
                                tmpShop.setShopCategory(xmlPullParser.nextText());
                                break;
                            case "url_mobile":
                                tmpShop.setShopUrl(xmlPullParser.nextText());
                                break;
                            case "shop_image1":
                                tmpShop.setShopImage(xmlPullParser.nextText());
                                break;
                            case "address":
                                tmpShop.setShopAddress(xmlPullParser.nextText());
                                break;
                            case "opentime":
                                tmpShop.setOpenTime(xmlPullParser.nextText());
                                //新しい店の情報をセット
                                shopList.get(i).shop.set(shopList.get(i).shop.size() - 1, tmpShop);
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.d("XmlPullParserSampleUrl", "Error xmlWriteInList");
        }
    }

    private ArrayList<RangeList> shopListDividing(){

        //カテゴリファイル読み込み

        String fastFood  = readTxtFile("fast_food.txt");
        String cafe      = readTxtFile("break.txt");
        String highCal   = readTxtFile("high_cal.txt");
        String highGrade = readTxtFile("high_grade.txt");
        String wine      = readTxtFile("wine.txt");
        String other     = readTxtFile("other.txt");

        //配列に区分け
//        String[] _fastFood  = fastFood.split(",", 0);
//        String[] _cafe      = cafe.split(",", 0);
//        String[] _highCal   = highCal.split(",", 0);
//        String[] _highGrade = highGrade.split(",", 0);
//        String[] _wine      = wine.split(",", 0);
//        String[] _other     = other.split(",", 0);

        String category = new String();
        ShopParameter shop = new ShopParameter();
        ArrayList<RangeList> rangeList = new ArrayList<RangeList>();

        for(int i = 0; i < RANGE; ++i){
            rangeList.add(new RangeList());
            int size = shopList.get(i).shop.size();
            for(int j = 0; j < size; ++j){
                //店情報とカテゴリ取得
                shop     = shopList.get(i).shop.get(j);
                category = shop.getShopCategory();

                //カテゴリごとに分ける
                //部分一致
                if (fastFood.contains(category)) {
                    shop.setShopCategoryType("category1");
                    rangeList.get(i).fastFood.add(shop);
                }else if (cafe.contains(category)) {
                    shop.setShopCategoryType("category2");
                    rangeList.get(i).cafe.add(shop);
                }else if (highCal.contains(category)) {
                    shop.setShopCategoryType("category3");
                    rangeList.get(i).highCal.add(shop);
                }else if (highGrade.contains(category)) {
                    shop.setShopCategoryType("category4");
                    rangeList.get(i).highGrade.add(shop);
                }else if (wine.contains(category)) {
                    shop.setShopCategoryType("category5");
                    rangeList.get(i).wine.add(shop);
                }else if (other.contains(category)) {
                    shop.setShopCategoryType("category6");
                    rangeList.get(i).other.add(shop);
                }else {
                    shop.setShopCategoryType("category6");
                    rangeList.get(i).other.add(shop);
                }
                //完全一致
//                if      (equalsString(category, _fastFood))  rangeList.get(i).fastFood.add(shop);
//                else if (equalsString(category, _cafe))      rangeList.get(i).cafe.add(shop);
//                else if (equalsString(category, _highCal))   rangeList.get(i).highCal.add(shop);
//                else if (category.contains("焼肉"))   rangeList.get(i).highCal.add(shop);
//                else if (equalsString(category, _highGrade)) rangeList.get(i).highGrade.add(shop);
//                else if (equalsString(category, _wine))      rangeList.get(i).wine.add(shop);
//                else if (category.contains("酒")) rangeList.get(i).highCal.add(shop);
//                else if (equalsString(category, _other))     rangeList.get(i).other.add(shop);
//                else                                         rangeList.get(i).other.add(shop);

            }
        }

        return rangeList;
    }

    //店リストのカテゴリ一覧作成
//    private String[][] categoryDividing(){
//        //一時的なカテゴリ一覧入力用
//        String str;
//        //店のカテゴリー入力用
//        String keyword;
//        //距離ごとに分けたカテゴリー一覧
//        String[][] category = new String[RANGE][];
//
//        for(int i = 0; i < RANGE; ++i){
//            //初めに","で区切ることで最初のカテゴリを判定できる
//            str = shopList.get(i).shop.get(0).getShopCategory() + ",";
//            //店の数ループする
//            int size = shopList.get(i).shop.size();
//            for(int j = 1;j < size;j++){
//                //カテゴリを取得
//                keyword = shopList.get(i).shop.get(j).getShopCategory();
//                //重複チェック
//                if(!str.contains(keyword)){
//                    //カテゴリ一覧に追加
//                    //str=今までの一覧、keyword=追加されるカテゴリ、","=区切り
//                    str = str + keyword + ",";
//                }
//            }
//            //一つの文字列を複数に分解
//            category[i] = str.split(",",0);
//        }
//        return  category;
//    }


    //カテゴリの完全一致時に使用
    private boolean equalsString(String str, String[] _str){
        for(int i = 0; i < _str.length; ++i){
            if(_str[i].equals(str)){
                return true;
            }
        }
        return false;
    }

    private String readTxtFile(String fileName){
        String text = new String();
        try{
            InputStream is = activity.getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String str;
            while((str = br.readLine()) != null){
                System.out.println(str);
                text += str;
            }

            is.close();
            br.close();
        }catch(FileNotFoundException e){
            Log.d("readFile", "file is not found");
        }catch (IOException e){
            Log.d("BufferReader", "buffer is not road");
        }catch (Exception e){
            Log.d("InputStream", "error");
        }

        return text;
    }

    // InputStream -> String
    private String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}