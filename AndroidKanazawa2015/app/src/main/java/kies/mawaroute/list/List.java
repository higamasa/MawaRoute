package kies.mawaroute.list;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.owner.kit.kies.androidkanazawa2015.R;

import java.util.ArrayList;

import kies.mawaroute.gnavi.ShopParameter;

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Activityがコールバックを実装しているかチェック
        if (activity instanceof FragmentTopCallback == false) {
            throw new ClassCastException("activity が FragmentTopCallback を実装していません.");
        }
        mCallback = (FragmentTopCallback) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        shopList = (ArrayList<ShopParameter>) bundle.getSerializable("ShopList");
        listView = (ListView) view.findViewById(R.id.list);
        /* データの作成 */
        objects = new ArrayList<CustomData>();
        size = shopList.size();
        for (int i = 0; i < size; i++) {
            switch (shopList.get(i).getShopCategoryType()) {
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
            item.setShopNameData(shopList.get(i).getShopName());
            item.setShopCategoryData(shopList.get(i).getShopCategory());
            objects.add(item);
            customAdapter = new CustomAdapter(activity, android.R.layout.simple_list_item_multiple_choice, objects, display);
            listView.setAdapter(customAdapter);
        }
        item = new CustomData();
        item.setImagaData(null);
        item.setShopNameData("Powered by ぐるなび");
        item.setShopCategoryData(null);
        objects.add(item);
        customAdapter = new CustomAdapter(activity, android.R.layout.simple_list_item_multiple_choice, objects, display);
        listView.setAdapter(customAdapter);

        //リスト項目が選択された時のイベントを追加
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (size > position) {
                    customAdapter.notifyDataSetChanged();
                    ListView listView = (ListView) parent;
                    SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
                    count = checkedItemPositions.size();
                    if (count <= 5) {
                        if (checkedItemPositions.get(position) == true) {
                            mCallback.listCallback(shopList.get(position), checkedItemPositions.get(position));
                        } else {
                            mCallback.listCallback(shopList.get(position), checkedItemPositions.get(position));
                            checkedItemPositions.delete(position);
                        }
                    } else {
                        checkedItemPositions.delete(position);
                        final Snackbar snackbar = Snackbar.make(view, "5個以上選択できません", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        snackbar.show();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
        listView.setAdapter(null);
        customAdapter.clear();
        customAdapter = null;
        image = null;
        item.setImagaData(null);
        item.setShopNameData(null);
        objects.clear();
    }

    public interface FragmentTopCallback {
        void listCallback(ShopParameter shopParameter, boolean bool);
    }

}