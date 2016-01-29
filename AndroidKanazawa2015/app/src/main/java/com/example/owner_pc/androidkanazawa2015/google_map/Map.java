package com.example.owner_pc.androidkanazawa2015.google_map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.owner_pc.androidkanazawa2015.R;

/**
 * Created by atsusuke on 2016/01/29
 * 村田篤亮.
 */
public class Map extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, null);
    }
}
