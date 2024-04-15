package com.example.sharebikes.Activity;

import android.app.AlertDialog;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.sharebikes.BmobDatabase.User;
import com.example.sharebikes.R;

import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;



import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


public class UserActivity extends BaseActivity {
    private ImageView ivAvatar;
    private TextView tvUsername, tvPhoneNumber, tvSex, tvBalance;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ImageView ivSex, ivBalance, ivPhone, ivUsername;
    User user;
    String objectId = "54c474760c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvUsername = findViewById(R.id.tv_username);
        tvPhoneNumber = findViewById(R.id.tv_phone);
        tvSex = findViewById(R.id.tv_sex);
        tvBalance = findViewById(R.id.tv_balance);

        ivSex = findViewById(R.id.iv_sex_go);
        ivBalance = findViewById(R.id.iv_balance_go);
        ivPhone = findViewById(R.id.iv_phone_go);
        ivUsername = findViewById(R.id.iv_username_go);

        ivSex.setOnClickListener(this::onSexImageViewClick);
        ivPhone.setOnClickListener(this::onPhoneImageViewClick);
        ivUsername.setOnClickListener(this::setUsernameImageViewClick);
        // 注册ActivityResultLauncherx
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            ivAvatar.setImageURI(selectedImageUri);
                            // Optionally, upload the image to Bmob as shown in previous examples
                        }
                    }
                }
        );

        setupListeners();
        loadUserData();
        // 设置更新操作
    }
    private void loadUserData() {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<User>() {
            @Override
            public void done(User object, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功");
                    runOnUiThread(() -> updateUserInterface(object));
                } else {
                    Log.i("bmob", "查询失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void updateUserInterface(User user) {
        this.user = user;
        Log.d("UserActivity", "updateUserInterface:name" + user.getUsername() + "phone" + user.getPhonenumber() + "sex" + user.getSex() + "balance" + user.getBalance() + "avatar" + user.getAvatar());
        tvUsername.setText(user.getUsername());
        tvPhoneNumber.setText(user.getPhonenumber());
        tvSex.setText(user.getSex());
        tvBalance.setText(String.valueOf(user.getBalance()));
        if (user.getAvatar() != null) {
            Glide.with(this).load(user.getAvatar()).into(ivAvatar);
        }
    }

    private void setupListeners() {
        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });
    }

    public void onSexImageViewClick(View view) {
        Toast.makeText(this, "点击了性别", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入新的性别");

        // 设置输入框
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // 设置确认按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newSex = input.getText().toString();
                // 在这里使用 Bmob 更新用户的性别数据
                updateUserSex(newSex);
            }
        });
        // 设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void updateUserSex(String newSex) {
        user.setSex(newSex);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    // 更新成功
                    Toast.makeText(UserActivity.this, "性别更新成功", Toast.LENGTH_SHORT).show();
                    // 更新界面上显示的性别数据
                    tvSex.setText(newSex);
                } else {
                    // 更新失败
                    Toast.makeText(UserActivity.this, "性别更新失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onPhoneImageViewClick(View view) {
        Toast.makeText(this, "点击了电话号码", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入新的电话号码");

        // 设置输入框
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        // 设置确认按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPhone = input.getText().toString();
                // 在这里使用 Bmob 更新用户的电话号码数据
                updateUserPhone(newPhone);
            }
        });
        // 设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void updateUserPhone(String newPhone) {
        user.setPhonenumber(newPhone);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    // 更新成功
                    Toast.makeText(UserActivity.this, "电话号码更新成功", Toast.LENGTH_SHORT).show();
                    // 更新界面上显示的电话号码数据
                    tvPhoneNumber.setText(newPhone);
                } else {
                    // 更新失败
                    Toast.makeText(UserActivity.this, "电话号码更新失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void setUsernameImageViewClick(View view) {
        Toast.makeText(this, "点击了用户名", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("输入新的用户名");

        // 设置输入框
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // 设置确认按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUsername = input.getText().toString();
                // 在这里使用 Bmob 更新用户的用户名数据
                updateUsername(newUsername);
            }
        });
        // 设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void updateUsername(String newUsername) {
        user.setUsername(newUsername);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    // 更新成功
                    Toast.makeText(UserActivity.this, "用户名更新成功", Toast.LENGTH_SHORT).show();
                    // 更新界面上显示的用户名数据
                    tvUsername.setText(newUsername);
                } else {
                    // 更新失败
                    Toast.makeText(UserActivity.this, "用户名更新失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
