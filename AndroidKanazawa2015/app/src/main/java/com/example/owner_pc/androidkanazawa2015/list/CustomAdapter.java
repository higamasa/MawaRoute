package com.example.owner_pc.androidkanazawa2015.list;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.owner_pc.androidkanazawa2015.R;

import java.util.*;

/**
 * Created by atsusuke on 2016/02/02.
 */
public class CustomAdapter extends ArrayAdapter<CustomData>{
    private LayoutInflater layoutInflater_;

    public CustomAdapter(Context context, int textViewResourceId, java.util.List<CustomData> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 特定の行(position)のデータを得る
        CustomData item = (CustomData)getItem(position);

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.image_item, parent, false);
        }

        // CustomDataのデータをViewの各Widgetにセットする
        ImageView imageView;
        imageView = (ImageView)convertView.findViewById(R.id.image);
        imageView.setImageBitmap(item.getImageData());

        TextView textView;
        textView = (TextView)convertView.findViewById(R.id.text);
        textView.setText(item.getTextData());

        Context context = getContext();
        ListView listView = (ListView)parent;
        Resources resources = context.getResources();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();


        if (checkedItemPositions.get(position) == true) {
            convertView.setBackgroundColor(Color.rgb(176, 224, 230));
        } else {
            convertView.setBackgroundColor(Color.rgb(255, 255, 255));
        }

        return convertView;
    }

}
