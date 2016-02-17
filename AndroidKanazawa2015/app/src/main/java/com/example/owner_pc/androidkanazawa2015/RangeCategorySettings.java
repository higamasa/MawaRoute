package com.example.owner_pc.androidkanazawa2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.owner_pc.androidkanazawa2015.gnavi.SettingParameter;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;

/**
 * Created by nns on 2016/02/09.
 */
public class RangeCategorySettings extends AppCompatActivity implements View.OnClickListener{

    private RadioGroup _radioGroup;
    private SettingParameter _settingParam = new SettingParameter();
    private ShopCtrl _shopCtrl = new ShopCtrl();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.range_category_settings);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
        _radioGroup = (RadioGroup)findViewById(R.id.RadioGroup);
        checkradio();
    }

    // ラジオボタン
    public void checkradio(){
        _radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);
                //todo if文がスルーされとる
                if (radioButton.getText() == "300") {
                    System.out.println(_settingParam.getRangeType());
                }
                if (radioButton.getText() == "500") {
                    System.out.println(_settingParam.getRangeType());
                }
                if (radioButton.getText() == "1000") {
                    System.out.println(_settingParam.getRangeType());
                }
                Log.d("radiobutton", "onCheckedChanged():" + radioButton.getId());
            }
        });

    }


    // チェックボタン
    //todo on,off処理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.high_cal:
                _settingParam.setHighCal(true);
                break;
            case R.id.fastfood:
                _settingParam.setFastFood(true);
                break;
            case R.id.other:
                _settingParam.setOther(true);
                break;
            case R.id.wine:
                _settingParam.setWine(true);
                break;
            case R.id.high_grade:
                _settingParam.setHighGrade(true);
                break;
            case R.id.cafe:
                _settingParam.setCafe(true);
                break;
        }
    }

    public void onClick_ok(View v){
        _shopCtrl.categoryDividing();
        Intent data = new Intent();
        data.putExtra("ShopList", _shopCtrl.getShopList());
        setResult(RESULT_OK, data);
        finish();

        //Toast.makeText(RangeCategorySettings.this, "設定を変更しました", Toast.LENGTH_SHORT).show();
    }

    public void onClickCansel(View v){
        finish();
    }



}
