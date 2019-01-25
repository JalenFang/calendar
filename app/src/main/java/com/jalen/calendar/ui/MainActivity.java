package com.jalen.calendar.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jalen.calendar.R;
import com.jalen.calendar.util.LocalCalendarUtil;

public class MainActivity extends AppCompatActivity {

    private static final int CALENDAR_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, CALENDAR_PERMISSION_REQUEST_CODE);
    }

    //获取本地日历账号
    public void getLocalCalendar(View v) {
        LocalCalendarUtil.getInstance().getLocalCalendar(this);
    }

    //添加本地日历账户
    public void addLocalCalendar(View v) {
        LocalCalendarUtil.getInstance().addLocalCalendar(this);
    }

    //删除本地日历账户
    public void deleteLocalCalendar(View v) {
        LocalCalendarUtil.getInstance().deleteLocalCalendar(this, 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            switch (requestCode) {
                case CALENDAR_PERMISSION_REQUEST_CODE:
                    int grantResult = grantResults[0];
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "获得权限", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "没有日历的读写权限", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
            }
        }
    }
}
