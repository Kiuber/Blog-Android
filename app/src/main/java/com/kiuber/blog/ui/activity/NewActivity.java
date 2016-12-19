package com.kiuber.blog.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.kiuber.blog.R;
import com.kiuber.blog.bean.Tint;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Kiuber on 2016/12/2.
 */
public class NewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvDevice;
    private TextView mTvLocation;
    private TextView mTvAddress;
    private EditText mEtContent;
    private TextView mBtnRelease;

    private AMapLocationClient mLocationClient = null;
    private AMapLocationListener mLocationListener = null;
    private AMapLocationClientOption mLocationOption = null;

    private String mStrLocationAll;
    private Spinner mSpLabel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        initView();
        amap();
    }


    private void amap() {
        // 高德地图
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation arg0) {
                if (arg0 != null) {
                    if (arg0.getErrorCode() == 0) {
                        bundleData(arg0);
                    } else {
                        Toast.makeText(NewActivity.this, arg0.getErrorInfo(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        // 初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        // 设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setWifiActiveScan(true);
        mLocationOption.setMockEnable(false);
        mLocationOption.setInterval(2000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }


    private void bundleData(AMapLocation arg0) {
        mTvAddress.setText(arg0.getAddress());
        mTvLocation.setText(arg0.getLocationType() + "\n" + "纬度："
                + arg0.getLatitude() + "\n" + "经度："
                + arg0.getLongitude() + "\n" + "精度：" + arg0.getAccuracy());
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date(arg0.getTime());
        df.format(date);// 定位时间
        mStrLocationAll = "定位类型："
                + arg0.getLocationType() + "\n" + "纬度："
                + arg0.getLatitude() + "\n" + "经度："
                + arg0.getLongitude() + "\n" + "精度："
                + arg0.getAccuracy() + "\n" + "定位时间："
                + df.format(date) + "\n" + "地址："
                + arg0.getAddress() + "\n" + "国家："
                + arg0.getCountry() + "\n" + "省："
                + arg0.getProvince() + "\n" + "城市："
                + arg0.getCity() + "\n" + "城区信息："
                + arg0.getDistrict() + "\n" + "街道信息："
                + arg0.getStreet() + "\n" + "街道门牌号信息："
                + arg0.getStreetNum() + "\n" + "城市编码："
                + arg0.getCityCode() + "\n" + "地区编码："
                + arg0.getAdCode();
    }

    private void initView() {
        mTvDevice = (TextView) findViewById(R.id.tv_device);
        mTvLocation = (TextView) findViewById(R.id.tv_location);
        mTvAddress = (TextView) findViewById(R.id.tv_address);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mBtnRelease = (TextView) findViewById(R.id.btn_release);
        mSpLabel = (Spinner) findViewById(R.id.sp_label);
        mBtnRelease.setOnClickListener(this);
    }

    private void release2Bmob() {
        Tint tint = new Tint();
        tint.setDevice(mTvDevice.getText().toString());
        tint.setLocation(mTvLocation.getText().toString());
        tint.setAddress(mTvAddress.getText().toString());
        tint.setContent(mEtContent.getText().toString());
        tint.setLocation_all(mStrLocationAll);
        tint.setLabel(mSpLabel.getSelectedItem().toString());
        tint.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(NewActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_address:
                amap();
                break;
            case R.id.btn_release:
                release2Bmob();
                break;
        }
    }
}
