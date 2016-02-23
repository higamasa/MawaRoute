package com.example.owner_pc.androidkanazawa2015.list;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.owner_pc.androidkanazawa2015.R;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopParameter;
import java.util.ArrayList;
/**
 * Created by atsusuke on 2016/02/01.
 */
public class List extends Fragment {
    View view;
    Bitmap image;
    Activity activity;
    private ArrayList<CustomData> objects;
    private ArrayList<ShopParameter> shopList = new ArrayList<ShopParameter>();
    private CustomAdapter customAdapter;
    private int size;
    private FragmentTopCallback mCallback;
    private CustomData item;
    private ListView listView;
    private int count;

    public interface FragmentTopCallback {
        void listCallback(ShopParameter shopParameter, boolean bool);
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
        view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Bundle bundle = getArguments();
        shopList = (ArrayList<ShopParameter>)bundle.getSerializable("ShopList");
        //Log.d("check", String.valueOf(shopCtrl.getShopList().get(away).shop.size()));
        listView = (ListView)view.findViewById(R.id.list);
        /* データの作成 */
        objects = new ArrayList<CustomData>();
        size = shopList.size();
        for (int i = 0; i < size; i++){
            Log.d("check", shopList.get(i).getShopCategoryType());
            switch (shopList.get(i).getShopCategoryType()){
                case "category1":
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.category1);
                    break;
                case "category2":
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.category2);
                    break;
                case "category3":
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.category3);
                    break;
                case "category4":
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.category4);
                    break;
                case "category5":
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.category5);
                    break;
                case "category6":
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.category6);
                    break;
            }
            item = new CustomData();
            item.setImagaData(image);
            item.setTextData(shopList.get(i).getShopName());
//            shopParameter = shopList.get(i);
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
                if (size > position) {
                    customAdapter.notifyDataSetChanged();
                    ListView listView = (ListView) parent;
                    SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
                    //String msg = String.format("position:%d check:%b", position, checkedItemPositions.get(position));
                    //Log.d("position", String.valueOf(size));
                    //Log.d("position", msg);
                    count = checkedItemPositions.size();
                    if (count <= 5) {
                        Toast.makeText(getActivity(), count + " / 5", Toast.LENGTH_SHORT).show();
                        if (checkedItemPositions.get(position) == true) {
                            mCallback.listCallback(shopList.get(position), checkedItemPositions.get(position));
                        } else {
                            mCallback.listCallback(shopList.get(position), checkedItemPositions.get(position));
                            checkedItemPositions.delete(position);
                        }
                    }else {
                        checkedItemPositions.delete(position);
                        Toast.makeText(getActivity(), "5 / 5", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        view = null;
        listView.setAdapter(null);
        customAdapter.clear();
        customAdapter = null;
        image = null;
        item.setImagaData(null);
        item.setTextData(null);
        objects.clear();
    }

}