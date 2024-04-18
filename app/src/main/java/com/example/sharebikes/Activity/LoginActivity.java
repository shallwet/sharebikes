package com.example.sharebikes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharebikes.BmobDatabase.User;
import com.example.sharebikes.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

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
        checkLogin(phone, password);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // 简单的验证中国大陆的手机号格式
        return phoneNumber.matches("^1[3-9]\\d{9}$");
    }

    private boolean isValidPassword(String password) {
        // 密码长度至少为8，包含字母和数字
        return password.matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
    }

    public void checkLogin(String phone, String password) {
        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("phonenumber", phone);
        userBmobQuery.addWhereEqualTo("password", password);
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    Log.d("BMOB", "查询成功"+object.size());
                    if (!object.isEmpty()) { // 确保至少有一个对象
                        User user1 = object.get(0); // 获取第一个对象
                        String objectId = user1.getObjectId(); // 获取objectId
                        Log.d("BMOB", "objectId: " + objectId);
                        // 此处可以继续使用objectId进行其他操作
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("objectId", objectId); // 使用putExtra方法传递数据
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("BMOB", "查询成功，但无数据");
                        Toast.makeText(LoginActivity.this, "登录失败无数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("BMOB", e.toString());
                    Toast.makeText(LoginActivity.this, "登录失败,不存在该用户", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}