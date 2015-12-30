package com.example.owner_pc.androidkanazawa2015;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by atsusuke on 2015/12/31.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "L", "G", "R", "S" };
    private Context context;


    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch(arg0){
            case 0:
                TestPage1 page1 = new TestPage1();
                return page1;

            case 1:
                TestPage2 page2 = new TestPage2();
                return page2;

            case 2:
                TestPage3 page3 = new TestPage3();
                return page3;
            case 3:
                TestPage4 page4 = new TestPage4();
                return page4;

        }
        return null;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}

