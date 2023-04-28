package com.example.readingsms;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "abc";
    private Button btn_readsms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
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
