package com.example.owner_pc.androidkanazawa2015;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;
import com.example.owner_pc.androidkanazawa2015.google_map.Map;
import com.example.owner_pc.androidkanazawa2015.list.List;
import java.io.Serializable;
/**
 * Created by atsusuke on 2015/12/31.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter implements Serializable {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "L", "M", "R" };
    private Context context;
    private double latitude;
    private double longitude;
    private ShopCtrl shopCtrl;

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
            case 0:
                bundle.putSerializable("shopCtrl", shopCtrl);
                List list = new List();
                list.setArguments(bundle);
                return list;
            case 1:
                bundle.putDouble("latitude" , latitude);
                bundle.putDouble("longitude" , longitude);
                bundle.putSerializable("shopCtrl", shopCtrl);
                Map map = new Map();
                map.setArguments(bundle);
                return map;
            case 2:
                return new RoulettePage();
        }
        return null;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

