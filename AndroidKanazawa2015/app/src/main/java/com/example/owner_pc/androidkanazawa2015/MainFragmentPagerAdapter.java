package com.example.owner_pc.androidkanazawa2015;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopList;
import com.example.owner_pc.androidkanazawa2015.google_map.Map;
import com.example.owner_pc.androidkanazawa2015.list.List;
import java.io.Serializable;
/**
 * Created by atsusuke on 2015/12/31.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter implements Serializable {
    final int PAGE_COUNT = 3;
    private Context context;
    private double latitude;
    private double longitude;
    private ShopCtrl shopCtrl;
    private int[] data = new int[5];
    private ShopList shopList = new ShopList();


    public MainFragmentPagerAdapter(FragmentManager fm, Context context , double latitude , double longitude ,
                                    ShopCtrl shopCtrl) {
        super(fm);
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
        this.shopCtrl = shopCtrl;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int arg0) {
        Bundle bundle = new Bundle();
        switch(arg0){
            //リスト
            case 0:
                bundle.putSerializable("shopCtrl", shopCtrl);
                List list = new List();
                list.setArguments(bundle);
                return list;
            //マップ
            case 1:
                //todo ショップリスト渡す
                bundle.putDouble("latitude" , latitude);
                bundle.putDouble("longitude" , longitude);
                bundle.putSerializable("shopCtrl", shopCtrl);
                Map map = new Map();
                map.setArguments(bundle);
                return map;
//                return new TestPage1();
            //ルーレット
            case 2:
                //todo ショップリスト渡す
                bundle.putIntArray("data", data);
                RoulettePage roulettePage = new RoulettePage();
                roulettePage.setArguments(bundle);
                return roulettePage;
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void destroyAllItem(ViewPager pager) {
        for (int i = 1; i < getCount(); i++) {
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
            Object objectobject = this.instantiateItem(pager, 0);
            if (objectobject != null)
                destroyItem(pager, 0, objectobject);
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
            trans.commit();
        }
    }

    public void setData(int[] data){
        this.data = data;
    }
    public void setShopList(ShopList shopList){
        this.shopList = shopList;
    }
    public void setShopCtrl(ShopCtrl shopCtrl){
        this.shopCtrl = shopCtrl;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        //return tabTitles[position];
        return null;
    }

    public Fragment findFragmentByPosition(ViewPager viewPager, int position) {
        return (Fragment) instantiateItem(viewPager, position);
    }

}

