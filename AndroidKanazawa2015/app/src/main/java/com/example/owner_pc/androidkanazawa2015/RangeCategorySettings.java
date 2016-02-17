package com.example.owner_pc.androidkanazawa2015;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.owner_pc.androidkanazawa2015.gnavi.SettingParameter;
import com.example.owner_pc.androidkanazawa2015.gnavi.ShopCtrl;

/**
 * Created by nns on 2016/02/09.
 */
public class RangeCategorySettings extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup _radioGroup;
    private SettingParameter _settingParam = new SettingParameter();
    private ShopCtrl _shopCtrl = new ShopCtrl();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setTitle("絞り込み設定");
        setContentView(R.layout.range_category_settings);
        _radioGroup = (RadioGroup)findViewById(R.id.RadioGroup);
        _radioGroup.setOnCheckedChangeListener(this);
        _radioGroup.check(R.id.RadioButton1);
    }

    // ラジオボタン
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //int CheckedId = _radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(checkedId);
        if(radioButton.isChecked()) {
            if (checkedId == R.id.RadioButton1) {
                _settingParam.setRangeType(0);
                //Log.d("radioCheck1", "radio1 Checking Complete!");
            } else if (checkedId == R.id.RadioButton2) {
                _settingParam.setRangeType(1);
                //Log.d("radioCheck2", "radio2 Checking Complete!");
            } else if (checkedId == R.id.RadioButton3) {
                _settingParam.setRangeType(2);
                //Log.d("radioCheck3", "radio3 Checking Complete!");
            }
        } else {
            _settingParam.setRangeType(0);
        }
    }

    // チェックボタン
    //todo on,off処理
    public void onCheckBoxClick(View v) {
        final boolean checked = ((CheckBox) v).isChecked();
        switch (v.getId()){
            case R.id.high_cal:
                if(checked) {
                    _settingParam.setHighCal(true);
                    Log.d("Check CheckBox Click", "set Completed!");
                } else {
                    _settingParam.setHighCal(false);
                    Log.d("Check CheckBox Click", "unset Completed!");
                }
                break;
            case R.id.fastfood:
                if(checked) {
                    _settingParam.setFastFood(true);
                } else {
                    _settingParam.setFastFood(false);
                }
                break;
            case R.id.other:
                if(checked) {
                    _settingParam.setOther(true);
                } else {
                    _settingParam.setOther(false);
                }
                break;
            case R.id.wine:
                if(checked) {
                    _settingParam.setWine(true);
                } else {
                    _settingParam.setWine(false);
                }
                break;
            case R.id.high_grade:
                if(checked) {
                    _settingParam.setHighGrade(true);
                } else {
                    _settingParam.setHighGrade(false);
                }
                break;
            case R.id.cafe:
                if(checked) {
                    _settingParam.setCafe(true);
                } else {
                    _settingParam.setCafe(false);
                }
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
