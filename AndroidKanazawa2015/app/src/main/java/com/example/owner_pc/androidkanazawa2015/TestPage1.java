package com.example.owner_pc.androidkanazawa2015;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by atsusuke on 2015/12/31.
 * テスト用のページです
 */
public class TestPage1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.testpage1, null);
    }
}
