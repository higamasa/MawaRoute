package com.owner.kit.kies.androidkanazawa2015.list;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.owner.kit.kies.androidkanazawa2015.R;

/**
 * Created by atsusuke on 2016/02/02.
 * ネクサス x1200 y1824
 * アローズ x1440 y2368
 */
public class CustomAdapter extends ArrayAdapter<CustomData>{
    private LayoutInflater layoutInflater_;
    private Point size = new Point();

    public CustomAdapter(Context context, int textViewResourceId, java.util.List<CustomData> objects , Display display) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        display.getSize(size);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pad = size.y/48;
        // 特定の行(position)のデータを得る
        CustomData item = (CustomData)getItem(position);
        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.image_item, parent, false);
        }
        // CustomDataのデータをViewの各Widgetにセットする
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        final TextView shopname_text = (TextView)convertView.findViewById(R.id.shopname_text);
        final TextView shopcategory_text = (TextView)convertView.findViewById(R.id.shopcategory_text);
        shopname_text.setTypeface(Typeface.SERIF);
        shopcategory_text.setTypeface(Typeface.SERIF);
        Bitmap bitmap = item.getImageData();
        if (bitmap != null) {
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, size.x / 6, size.x / 6, false));
            imageView.setPadding((pad + 20), pad, pad, pad);
            if (item.getShopNameData().length() <= 7) {
                shopname_text.setTextSize(1000/pad);
            }else {
                shopname_text.setTextSize(800/pad);
            }
            shopname_text.setPadding((pad/3), pad, 0, 0);
            shopcategory_text.setTextSize(600 / pad);
            shopcategory_text.setPadding((pad/3), 0, 0, (pad*4/5));
            shopcategory_text.setText("CATEGORY : " + item.getShopCategoryData());
        }else {
            imageView.setImageBitmap(bitmap);
            shopname_text.setTextSize(600/(size.y/32));
            imageView.setPadding(0,0,0,0);
            shopname_text.setPadding(32, 10, 10, 10);
            shopcategory_text.setText(item.getShopCategoryData());
        }
        shopname_text.setText(item.getShopNameData());
        Context context = getContext();
        ListView listView = (ListView)parent;
        Resources resources = context.getResources();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        if (checkedItemPositions.get(position) == true && bitmap != null) {
            convertView.setBackgroundColor(resources.getColor(R.color.colorGold));
        } else {
            convertView.setBackgroundColor(resources.getColor(R.color.colorYellow));
        }
        return convertView;
    }
}
