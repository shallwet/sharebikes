package com.example.sharebikes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.sharebikes.BmobDatabase.User;
import com.example.sharebikes.R;


import java.util.Locale;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends BaseActivity {

    private EditText etUsername, etPhoneNumber, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 初始化视图组件
        etUsername = findViewById(R.id.etUsername);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });
    }

    public boolean addUser() {
        String username = etUsername.getText().toString().trim();
        String phonenumber = etPhoneNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhonenumber(phonenumber);
        user.setSex("男");
        user.setBalance(0.0);
        user.setAvatar("https://first-photo-1306445803.cos.ap-nanjing.myqcloud.com/img/3ea7245c02668e29.jpg");
        user.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Log.d("User", "新增成功：" + objectId);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("User", "新增失败：" + e.getMessage());
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return false;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // 简单的验证中国大陆的手机号格式
        return phoneNumber.matches("^1[3-9]\\d{9}$");
    }

    private boolean isValidPassword(String password) {
        // 密码长度至少为8，包含字母和数字
        return password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
    }
}