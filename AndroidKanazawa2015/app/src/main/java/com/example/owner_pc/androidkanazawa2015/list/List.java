package com.example.owner_pc.androidkanazawa2015.list;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.owner_pc.androidkanazawa2015.R;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopList;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by atsusuke on 2016/02/01.
 */
public class List extends Fragment{
    View view;
    Bitmap image;
    private CustomAdapter customAdapter;
    Activity activity;

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

        Bundle bundle = getArguments();
        ShopCtrl shopCtrl = (ShopCtrl)bundle.getSerializable("shopCtrl");
        Log.d("check", String.valueOf(shopCtrl.getShopList().size()));

        final ListView listView = (ListView)view.findViewById(R.id.list);
        /* データの作成 */
        final ArrayList<CustomData> objects = new ArrayList<CustomData>();
        image = BitmapFactory.decodeResource(getResources(), R.drawable.cir_g);
        for (int i=0; i < 100; i++){
            CustomData item = new CustomData();
            item.setImagaData(image);
            item.setTextData("Example");
            objects.add(item);
            customAdapter = new CustomAdapter(activity, android.R.layout.simple_list_item_multiple_choice, objects);
            listView.setAdapter(customAdapter);
        }

        //リスト項目が選択された時のイベントを追加
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customAdapter.notifyDataSetChanged();
                ListView listView = (ListView) parent;
                SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
                String msg = String.format("position:%d check:%b", position, checkedItemPositions.get(position));
                Log.d("position", msg);
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
