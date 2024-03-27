package com.example.sharebikes.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.sharebikes.R;


import java.util.Locale;

public class RegisterActivity extends BaseActivity {

    private EditText etUsername, etPhoneNumber, etPassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 初始化视图组件
        etUsername = findViewById(R.id.etUsername);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String phonenumber = etPhoneNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if(username.isEmpty() || phonenumber.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = dbHelper.addUser(username, password, phonenumber, "N/A", 0.0, currentDate, currentDate);

        if(result) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity after successful registration
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }
    }
}