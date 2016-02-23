package com.example.owner_pc.androidkanazawa2015.list;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.Layout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.owner_pc.androidkanazawa2015.R;

/**
 * Created by atsusuke on 2016/02/02.
 */
public class CustomAdapter extends ArrayAdapter<CustomData> {
    private LayoutInflater layoutInflater_;
    private Point size = new Point();

    public CustomAdapter(Context context, int textViewResourceId, java.util.List<CustomData> objects, Display display) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        display.getSize(size);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 特定の行(position)のデータを得る
        final int pad = size.y / 48;

        CustomData item = (CustomData) getItem(position);

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.image_item, parent, false);
        }

        // CustomDataのデータをViewの各Widgetにセットする
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        textView.setTypeface(Typeface.SERIF);
        Bitmap bitmap = item.getImageData();

        if (bitmap != null) {
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, size.x / 7, size.x / 7, false));
            textView.setTextSize(1500 / (size.y / 32));
            imageView.setPadding(pad + 5, pad, pad, pad);
            textView.setPadding(pad / 3, pad, 0, 0);
        } else {
            imageView.setImageBitmap(null);
            textView.setTextSize(800 / (size.y / 32));
            imageView.setPadding(0, 0, 0, 0);
            textView.setPadding(32, 10, 10, 10);
        }

        textView.setText(item.getTextData());
        Context context = getContext();
        ListView listView = (ListView) parent;
        Resources resources = context.getResources();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();

        if (checkedItemPositions.get(position) && bitmap != null) {
            convertView.setBackgroundColor(resources.getColor(R.color.colorGold));
        } else {
            convertView.setBackgroundColor(resources.getColor(R.color.colorYellow));
        }

        return convertView;
    }
}
