package com.kiuber.blog.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kiuber.blog.R;
import com.kiuber.blog.util.SharedPreferenceUtil;

/**
 * Created by Kiuber on 2016/12/28.
 */
public class SmsActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_SMS_CODE = 123;
    private static final int CHANGE_DEFAULT_SMS = 234;
    private Spinner mSpType;
    private EditText mEtTel;
    private EditText mEtBody;
    private String mStrTel;
    private String mStrBody;
    private String defaultSmsPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        initView();
        initData();
        defaultSmsPackage = SharedPreferenceUtil.getOne(this, "app_config", "sms_default");
    }

    private void initData() {
        mSpType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"别人给自己发短信", "自己给别人发短信"}));
    }

    private void initView() {
        findViewById(R.id.btn_write).setOnClickListener(this);
        findViewById(R.id.btn_recovery).setOnClickListener(this);
        mSpType = (Spinner) findViewById(R.id.sp_type);
        mEtTel = (EditText) findViewById(R.id.et_tel);
        mEtBody = (EditText) findViewById(R.id.et_body);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_write:
                mStrTel = mEtTel.getText().toString();
                mStrBody = mEtBody.getText().toString();
                if (mStrTel.equals("")) {
                    Toast.makeText(this, "请输入手机号！", Toast.LENGTH_SHORT).show();
                } else if (mStrBody.equals("")) {
                    Toast.makeText(this, "请输入短信内容！", Toast.LENGTH_SHORT).show();
                } else {
                    defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(this);
                    //检查权限
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                            == PackageManager.PERMISSION_DENIED) {
                        //检查安卓版本
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            SharedPreferenceUtil.putOne(this, "app_config", "sms_default", defaultSmsPackage);
                            String packageName = getPackageName();
                            // 检查默认短信app
                            if (!defaultSmsPackage.equals(packageName)) {
                                selectDefaultApp(packageName);
                            } else {
                                writeSms();
                                Toast.makeText(this, "已经是默认短信应用！", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //安卓版本小于4.4，应该是用户点击了拒绝写入短信。
                            Toast.makeText(this, "请到系统设置里寻允许App写入短信", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        writeSms();
                    }
                }
                break;
            case R.id.btn_recovery:
                selectDefaultApp(defaultSmsPackage);
                break;
        }

    }

    private void selectDefaultApp(String packageName) {
        Intent intent =
                new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                packageName);
        startActivityForResult(intent, CHANGE_DEFAULT_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_SMS_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "失败" + grantResults[0], Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHANGE_DEFAULT_SMS:
                // NO:0 YES:-1
                if (resultCode == -1) {
                    writeSms();
                } else {
                    Toast.makeText(this, "设置为默认短信应用失败！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void writeSms() {
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://sms/");
        ContentValues values = new ContentValues();
        values.put("address", mStrTel);
        values.put("type", mSpType.getSelectedItemPosition() + 1);//1:接收;2:发送
        values.put("read", 0); //0:未读;1:已读
        values.put("body", mStrBody);
        values.put("date", System.currentTimeMillis());
        //启动短信
        resolver.insert(uri, values);
        Toast.makeText(this, "写入成功！", Toast.LENGTH_SHORT).show();
        if (defaultSmsPackage != null && !defaultSmsPackage.equals("")) {
            Intent intent = getPackageManager().getLaunchIntentForPackage(defaultSmsPackage);
            startActivity(intent);
        } else {
        }
    }
}
