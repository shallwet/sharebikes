package com.example.sharebikes.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.sharebikes.BmobDatabase.User;
import com.example.sharebikes.R;
import com.amap.api.maps.MapView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UploadFileListener;

public class MainActivity extends BaseActivity {
    MapView mMapView = null;
    private DrawerLayout drawerLayout; // 滑动菜单
    private Toolbar toolbar; // 工具栏
    private NavigationView navView; // 导航视图
    private AMap aMap = null; // 声明AMap对象
    private ImageButton scanButton; // 扫码按钮
    public String defaultAvatarUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MapsInitializer.updatePrivacyShow(this,true,true);
//        MapsInitializer.updatePrivacyAgree(this,true);
        setContentView(R.layout.activity_main);
        // 初始化组件
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);
        scanButton = findViewById(R.id.scan_button);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        // 设置导航视图头部布局点击事件
        View headerView = navView.getHeaderView(0);
        headerView.setOnClickListener(view -> showMsg("点击了头部布局"));
        // 设置导航视图菜单点击事件
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.item_user) {
                showMsg("进入个人中心");
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
                finish(); // Close the activity after successful registration
            } else if (id == R.id.item_order) {
                showMsg("点击了我的订单");
            } else if (id == R.id.item_wallet) {
                showMsg("点击了我的钱包");
            } else if (id == R.id.item_guidance) {
                showMsg("点击了使用指南");
            } else if (id == R.id.item_navigation) {
                showMsg("进入导航界面");
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
            // 点击任何项之后关闭抽屉
            drawerLayout.closeDrawer( GravityCompat.START);
            return true;
        });

        // 设置工具栏按钮点击事件，打开抽屉
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        // 设置扫码按钮点击事件运行adduser方法
        scanButton.setOnClickListener(view -> addUser("testuser2", "123456", "19275464058"));
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        // 初始化地图控制器对象
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(com.amap.api.maps.CameraUpdateFactory.zoomTo(15));
    }
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    public boolean addUser(String username, String password, String phonenumber) {
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
                    Toast.makeText(MainActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("User", "新增失败：" + e.getMessage());
                    Toast.makeText(MainActivity.this, "新增失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return false;
    }
}

