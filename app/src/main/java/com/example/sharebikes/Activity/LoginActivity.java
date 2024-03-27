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

        // 这里简化了登录验证逻辑，实际项目中应查询数据库验证用户凭据
        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "手机号或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 模拟一个简单的登录验证过程
        if ("demo".equals(phone) && "password".equals(password)) {
            // 登录成功
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // 结束当前活动
        } else {
            // 登录失败
            Toast.makeText(this, "登录失败：用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }
}