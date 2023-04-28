package com.example.readingsms;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "abc";
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Button btn_readsms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// android 6.0之后
            //如果未获得读取短信的权限
            if (this.checkSelfPermission("android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED)
                //请求读取短信的权限
                this.requestPermissions(new String[]{"android.permission.READ_SMS"}, PERMISSION_REQUEST_CODE);
        }
        initView();

    }

    //判断权限申请的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PERMISSION_REQUEST_CODE == (requestCode)) {//判断请求码
            for (int i = 0; i < grantResults.length; i++) {//逐一判断请求结果
                if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {//如果未申请到这一权限，则弹窗提示并跳出循环
                    Toast.makeText(this, "没有获得权限，无法继续！", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    }

    private void readSMS() {
        ContentResolver resolver = getContentResolver();//获取ContentResolver对象
        //创建一个光标对象cursor用来存放查询结果
        //参数1：URI
        //参数2：需要查询的列，如果是null则代表所有列
        //参数3：选择条件，用来选择满足条件的行
        //参数4：条件中的参数
        //参数5：排序
        Cursor cursor = resolver.query(Telephony.Sms.CONTENT_URI,
                new String[]{Telephony.Sms._ID, Telephony.Sms.PERSON, Telephony.Sms.DATE, Telephony.Sms.ADDRESS, Telephony.Sms.BODY},
                null,
                null,
                Telephony.Sms.DEFAULT_SORT_ORDER);
        while (cursor.moveToNext()) {//遍历查询结果
            String _id = cursor.getInt(0) + "";
            String person = cursor.getString(1);
            String date = cursor.getString(2);
            String address = cursor.getString(3);
            String body = cursor.getInt(4) + "";
            Log.i(TAG, "readSMS: _id:" + _id + " date:" + date + " address:" + address + " person:" + person + " body:" + body);
        }
        cursor.close();
    }

    private void initView() {
        btn_readsms = (Button) findViewById(R.id.btn_readsms);
        btn_readsms.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_readsms:
                readSMS();
                break;
        }
    }
}
