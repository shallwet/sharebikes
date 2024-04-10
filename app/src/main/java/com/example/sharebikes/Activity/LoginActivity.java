package com.example.sharebikes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharebikes.R;

public class LoginActivity extends BaseActivity {
    private EditText etPhone, etPsw;
    private Button btnLogin;
    private TextView tvRegister, tvFindPsw;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化组件
        etPhone = findViewById(R.id.et_phone);
        etPsw = findViewById(R.id.et_psw);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tvFindPsw = findViewById(R.id.tv_find_psw);

        dbHelper = new DatabaseHelper(this);

        // 登录按钮监听器
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // 注册链接监听器
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 可选：忘记密码处理
        tvFindPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 可以在这里添加找回密码的逻辑
                Toast.makeText(LoginActivity.this, "忘记密码功能待实现", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser() {
        String phone = etPhone.getText().toString().trim();
        String password = etPsw.getText().toString().trim();

        if (!isValidPhoneNumber(phone)) {
            Toast.makeText(LoginActivity.this, "无效的手机号格式", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(LoginActivity.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkLogin(phone, password)) {
            // 登录成功
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // 登录失败
            Toast.makeText(LoginActivity.this, "手机号或密码错误", Toast.LENGTH_SHORT).show();
        }
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