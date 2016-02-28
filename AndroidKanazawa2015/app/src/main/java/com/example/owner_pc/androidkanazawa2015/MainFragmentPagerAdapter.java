package com.example.owner_pc.androidkanazawa2015;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopParameter;
import com.example.owner_pc.androidkanazawa2015.google_map.Map;
import com.example.owner_pc.androidkanazawa2015.list.List;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by atsusuke on 2015/12/31.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter implements Serializable {
    final int PAGE_COUNT = 3;
    private Context context;
    private double latitude;
    private double longitude;
    private ArrayList<ShopParameter> shopList = new ArrayList<ShopParameter>();

    public MainFragmentPagerAdapter(FragmentManager fm, Context context , double latitude , double longitude ,
                                    ArrayList<ShopParameter> shopList) {
        super(fm);
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.shopList = shopList;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        switch(position){
            //リスト
            case 0:
                bundle.putSerializable("ShopList", shopList);
                List list = new List();
                list.setArguments(bundle);
                return list;
            //マップ
            case 1:
                //todo ショップリスト渡す
                bundle.putDouble("latitude" , latitude);
                bundle.putDouble("longitude" , longitude);
                bundle.putSerializable("ShopList", shopList);
                Map map = new Map();
                map.setArguments(bundle);
                return map;
            //ルーレット
            case 2:
                RoulettePage roulettePage = new RoulettePage();
                return roulettePage;
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void destroyAllItem(ViewPager pager) {
        for (int i = 0; i < getCount(); i++) {
            try {
                Object objectobject = this.instantiateItem(pager, i);
                if (objectobject != null)
                    destroyItem(pager, i, objectobject);
            } catch (Exception e) {

            }
        }
    }

    public void destroyListItem(ViewPager pager) {
        try {
            Object object = this.instantiateItem(pager, 0);
            if (object != null)
                destroyItem(pager, 0, object);
        } catch (Exception e) {

        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

        if (position <= getCount()) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment) object);
            trans.commitAllowingStateLoss();
        }
    }

    public void setShopList(ArrayList<ShopParameter> shopList){
        this.shopList = shopList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public Fragment findFragmentByPosition(ViewPager viewPager, int position) {
        return (Fragment) instantiateItem(viewPager, position);
    }

}

