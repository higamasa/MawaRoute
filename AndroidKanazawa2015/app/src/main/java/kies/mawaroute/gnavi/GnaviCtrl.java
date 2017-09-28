package kies.mawaroute.gnavi;

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
public class GnaviCtrl extends AsyncTask<Position, Void, String[]> {

    //3種類の距離(300m, 500m, 1000m)
    private static final int RANGE = 3;
    public ProgressDialog progressDialog;
    //コールバックインターフェース
    private AsyncTaskCallbacks callback = null;
    //プログレスダイアログ表示に必要
    private Activity activity = null;
    //距離ごとのリスト（5種類）、それぞれの中に店のリスト（複数）がある
    private ArrayList<ShopList> shopList = new ArrayList<ShopList>();

    public GnaviCtrl(Activity activity, AsyncTaskCallbacks callback) {
        this.activity = activity;
        this.callback = callback;
        for (int i = 0; i < RANGE; i++) {
            shopList.add(new ShopList());
        }
    }

    //XMLデータ取得前
    @Override
    protected void onPreExecute() {

        // プログレスダイアログの生成
        this.progressDialog = new ProgressDialog(this.activity);

        // プログレスダイアログの設定
        this.progressDialog.setMessage("読み込み中...");  // メッセージをセット

        // プログレスダイアログの表示
        this.progressDialog.show();
    }

    //XMLデータ取得
    @Override
    protected String[] doInBackground(Position... positions) {

        URL urls[] = new URL[RANGE];
        String[] result = new String[RANGE];
        Position position = positions[0];
        HttpURLConnection urlConnection = null;

        for (int i = 0; i < RANGE; i++) {
            try {
                urls[i] = new URL("http://api.gnavi.co.jp/RestSearchAPI/20150630/?format=xml&keyid=6ba07fbbdb9a380e560240a84d2da924&latitude=" + position.latitude + "&longitude=" + position.longitude + "&range=" + (i + 1) + "&hit_per_page=" + 500);
                urlConnection = (HttpURLConnection) urls[i].openConnection();
                result[i] = InputStreamToString(urlConnection.getInputStream());
            } catch (Exception e) {
                Log.d("XmlPullParserSampleUrl", "Error");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
        return result;
    }

    //XMLデータ取得後
    @Override
    protected void onPostExecute(String[] response) {

        //xmlデータをshopListに書き込む
        xmlWriteInList(response);

        //ShopListをカテゴリ分けし直す
        ArrayList<RangeList> rangeList = new ArrayList<RangeList>();
        ShopCtrl shopCtrl = new ShopCtrl();
        SettingParameter setting = new SettingParameter();

        //絞り込み条件を初期設定にする
        setting.initSetting();
        //ショップリストをカテゴリタイプ別に分ける
        rangeList = shopListDividing();
        shopCtrl.setRangeList(rangeList);
        //絞り込み条件のもとショップリスト生成
        shopCtrl.categoryDividing();

        // プログレスダイアログを閉じる
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }

        //終了をActivityに通知
        callback.onTaskFinished();
    }

    @Override
    protected void onCancelled() {
        Log.v("AsyncTask", "onCancelled");
        callback.onTaskCancelled();
    }

    //ぐるなびからとってきた店データをArrayListに格納
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

    //カテゴリ別に分け直す
    private ArrayList<RangeList> shopListDividing() {

        //カテゴリファイル読み込み
        String fastFood = readTxtFile("fast_food.txt");
        String cafe = readTxtFile("break.txt");
        String highCal = readTxtFile("high_cal.txt");
        String highGrade = readTxtFile("high_grade.txt");
        String wine = readTxtFile("wine.txt");
        String other = readTxtFile("other.txt");

        String category = new String();
        ShopParameter shop = new ShopParameter();
        ArrayList<RangeList> rangeList = new ArrayList<RangeList>();

        for (int i = 0; i < RANGE; ++i) {
            //範囲距離分(3個)追加
            rangeList.add(new RangeList());
            //ぐるなびからとってきた店数分解析
            int size = shopList.get(i).shop.size();
            for (int j = 0; j < size; ++j) {
                //店情報とカテゴリ取得
                shop = shopList.get(i).shop.get(j);
                category = shop.getShopCategory();

                //カテゴリごとに分ける
                //部分一致
                if (fastFood.contains(category)) {
                    shop.setShopCategoryType("category1");
                    rangeList.get(i).fastFood.add(shop);
                } else if (cafe.contains(category)) {
                    shop.setShopCategoryType("category2");
                    rangeList.get(i).cafe.add(shop);
                } else if (highCal.contains(category)) {
                    shop.setShopCategoryType("category3");
                    rangeList.get(i).highCal.add(shop);
                } else if (highGrade.contains(category)) {
                    shop.setShopCategoryType("category4");
                    rangeList.get(i).highGrade.add(shop);
                } else if (wine.contains(category)) {
                    shop.setShopCategoryType("category5");
                    rangeList.get(i).wine.add(shop);
                } else if (other.contains(category)) {
                    shop.setShopCategoryType("category6");
                    rangeList.get(i).other.add(shop);
                } else {
                    shop.setShopCategoryType("category6");
                    rangeList.get(i).other.add(shop);
                }
            }
        }

        return rangeList;
    }

    //txtファイル読み込み
    private String readTxtFile(String fileName) {
        String text = new String();
        try {
            InputStream is = activity.getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String str;
            while ((str = br.readLine()) != null) {
                System.out.println(str);
                text += str;
            }

            is.close();
            br.close();
        } catch (FileNotFoundException e) {
            Log.d("readFile", "file is not found");
        } catch (IOException e) {
            Log.d("BufferReader", "buffer is not road");
        } catch (Exception e) {
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