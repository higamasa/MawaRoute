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

    private static int CATEGORY_NUM = 6;
    private RadioGroup _radioGroup;
    private CheckBox[] checkBoxes = new CheckBox[CATEGORY_NUM];
    private SettingParameter _settingParam = new SettingParameter();
    private ShopCtrl _shopCtrl = new ShopCtrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("絞り込み設定");
        setContentView(R.layout.range_category_settings);
        initSetting();

    }

    private void initSetting() {
        //ラジオボタンの初期状態設定
        _radioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        _radioGroup.setOnCheckedChangeListener(this);
        switch (_settingParam.getRangeType()) {
            case 0:
                //300m
                _radioGroup.check(R.id.RadioButton1);
                break;
            case 1:
                //500m
                _radioGroup.check(R.id.RadioButton2);
                break;
            case 2:
                //1000m
                _radioGroup.check(R.id.RadioButton3);
        }

        //絞り込みの状態取得
        boolean[] checkFlag = new boolean[CATEGORY_NUM];
        checkFlag[0] = _settingParam.isHighCal();
        checkFlag[1] = _settingParam.isFastFood();
        checkFlag[2] = _settingParam.isOther();
        checkFlag[3] = _settingParam.isWine();
        checkFlag[4] = _settingParam.isHighGrade();
        checkFlag[5] = _settingParam.isCafe();

        //checkBoxのID指定
        checkBoxes[0] = (CheckBox) findViewById(R.id.high_cal);
        checkBoxes[1] = (CheckBox) findViewById(R.id.fastfood);
        checkBoxes[2] = (CheckBox) findViewById(R.id.other);
        checkBoxes[3] = (CheckBox) findViewById(R.id.wine);
        checkBoxes[4] = (CheckBox) findViewById(R.id.high_grade);
        checkBoxes[5] = (CheckBox) findViewById(R.id.cafe);

        //チェックボックスの初期状態設定
        for (int i = 0; i < CATEGORY_NUM; ++i) {
            checkBoxes[i].setChecked(checkFlag[i]);
        }
    }

    // ラジオボタン
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //int CheckedId = _radioGroup.getCheckedRadioButtonId();
        //RadioButton radioButton = (RadioButton) findViewById(checkedId);
        switch (checkedId) {
            case R.id.RadioButton1:
                _settingParam.setRangeType(0);
                //Log.d("radioCheck1", "radio1 Checking Complete!");
                break;
            case R.id.RadioButton2:
                _settingParam.setRangeType(1);
                //Log.d("radioCheck1", "radio2 Checking Complete!");
                break;
            case R.id.RadioButton3:
                _settingParam.setRangeType(2);
                //Log.d("radioCheck1", "radio3 Checking Complete!");
                break;
            default:
                _settingParam.setRangeType(0);
        }
    }

    // チェックボタン
    //todo on,off処理
    public void onCheckBoxClick(View v) {
        final boolean checked = ((CheckBox) v).isChecked();

        switch (v.getId()) {
            case R.id.high_cal:
                _settingParam.setHighCal(checked);
                if (checked) {
                    Log.d("Check CheckBox Click", "set Completed!");
                } else {
                    Log.d("Check CheckBox Click", "unset Completed!");
                }
                break;
            case R.id.fastfood:
                _settingParam.setFastFood(checked);
                break;
            case R.id.other:
                _settingParam.setOther(checked);
                break;
            case R.id.wine:
                _settingParam.setWine(checked);
                break;
            case R.id.high_grade:
                _settingParam.setHighGrade(checked);
                break;
            case R.id.cafe:
                _settingParam.setCafe(checked);
                break;
        }
    }

    public void onClick_ok(View v) {
        _shopCtrl.categoryDividing();
        Intent data = new Intent();
        data.putExtra("ShopList", _shopCtrl.getShopList());
        setResult(RESULT_OK, data);
        finish();

        //Toast.makeText(RangeCategorySettings.this, "設定を変更しました", Toast.LENGTH_SHORT).show();
    }

    public void onClickCancel(View v) {
        finish();
    }


}
