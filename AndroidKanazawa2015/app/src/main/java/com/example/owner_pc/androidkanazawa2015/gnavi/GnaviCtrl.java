package com.example.owner_pc.androidkanazawa2015.gnavi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import com.example.owner_pc.androidkanazawa2015.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
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
    private static final int RANGE = 5;

    public GnaviCtrl(Activity activity, AsyncTaskCallbacks callback){
        this.activity = activity;
        this.callback = callback;
        for(int i=0;i<5;i++) {
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
        ShopCtrl shopCtrl = new ShopCtrl();
        shopCtrl.setShopList(shopList);
        shopCtrl.setCategory(categoryDividing());

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

    private String[][] categoryDividing(){
        String str;
        String keyword;
        String[][] category = new String[RANGE][];
        for(int i = 0;i < RANGE;i++){
            str = shopList.get(i).shop.get(0).getShopCategory() + ",";
            int size = shopList.get(i).shop.size();
            for(int j = 1;j < size;j++){
                keyword = shopList.get(i).shop.get(j).getShopCategory();
                if(!str.contains(keyword)){
                    str = str + keyword + ",";
                }
            }
            category[i] = str.split(",",0);
        }
        return  category;
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