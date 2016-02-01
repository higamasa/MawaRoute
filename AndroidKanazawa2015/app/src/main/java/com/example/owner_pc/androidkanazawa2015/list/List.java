package com.example.owner_pc.androidkanazawa2015.list;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.owner_pc.androidkanazawa2015.R;
/**
 * Created by atsusuke on 2016/02/01.
 */
public class List extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container , false);
    }
}
