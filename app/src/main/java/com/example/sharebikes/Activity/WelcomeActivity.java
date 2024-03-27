package com.example.sharebikes.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.content.Intent;
import java.util.Timer;
import java.util.TimerTask;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sharebikes.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startMainActivity();
        // 创建数据库
        Log.d("DatabaseHelper", "开始调用语句");
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        dbHelper.getWritableDatabase();
        Log.d("DatabaseHelper", "调用语句成功");

    }

    private void startMainActivity(){

        TimerTask delayTask = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(mainIntent);
                WelcomeActivity.this.finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(delayTask,2000);//延时两秒执行 run 里面的操作
    }
}