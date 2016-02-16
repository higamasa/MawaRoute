package com.example.owner_pc.androidkanazawa2015.list;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.owner_pc.androidkanazawa2015.R;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopParameter;

import java.util.ArrayList;
/**
 * Created by atsusuke on 2016/02/01.
 */
public class List extends Fragment{
    View view;
    Bitmap image;
    Activity activity;
    ShopParameter shopParameter;
    private CustomAdapter customAdapter;
    private int away = 2;
    private int count;
    private FragmentTopCallback mCallback;
    private CustomData item;

    public interface FragmentTopCallback {
        void listCallback(ShopParameter shopParameter , boolean bool);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Activityがコールバックを実装しているかチェック
        if (activity instanceof FragmentTopCallback == false) {
            throw new ClassCastException("activity が FragmentTopCallback を実装していません.");
        }
        //
        mCallback = (FragmentTopCallback) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_fragment, container , false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Bundle bundle = getArguments();
        ShopCtrl shopCtrl = (ShopCtrl)bundle.getSerializable("shopCtrl");
        //Log.d("check", String.valueOf(shopCtrl.getShopList().get(away).shop.size()));
        ListView listView = (ListView)view.findViewById(R.id.list);
        /* データの作成 */
        ArrayList<CustomData> objects = new ArrayList<CustomData>();
        count = shopCtrl.getShopList().get(away).shop.size();
        image = BitmapFactory.decodeResource(getResources(), R.drawable.cir_g);
        for (int i = 0; i < count; i++){
            item = new CustomData();
            item.setImagaData(image);
            item.setTextData(shopCtrl.getShopList().get(away).shop.get(i).getShopName());
            shopParameter = shopCtrl.getShopList().get(away).shop.get(i);
            objects.add(item);
            customAdapter = new CustomAdapter(activity, android.R.layout.simple_list_item_multiple_choice, objects,display);
            listView.setAdapter(customAdapter);
        }
        item = new CustomData();
        item.setImagaData(null);
        item.setTextData("Powered by ぐるなび");
        objects.add(item);
        customAdapter = new CustomAdapter(activity, android.R.layout.simple_list_item_multiple_choice, objects,display);
        listView.setAdapter(customAdapter);

        //リスト項目が選択された時のイベントを追加
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (count > position) {
                    customAdapter.notifyDataSetChanged();
                    ListView listView = (ListView) parent;
                    SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
                    String msg = String.format("position:%d check:%b", position, checkedItemPositions.get(position));
                    Log.d("position", String.valueOf(count));
                    Log.d("position", msg);
                    mCallback.listCallback(shopParameter, checkedItemPositions.get(position));
                }
            }
        });
    }
}