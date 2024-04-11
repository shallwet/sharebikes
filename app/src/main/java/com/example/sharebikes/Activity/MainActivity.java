package com.example.sharebikes.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

//import com.amap.api.location.AMapLocationClient;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.google.android.material.navigation.NavigationView;
import com.example.sharebikes.R;


public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout; // 滑动菜单
    private Toolbar toolbar; // 工具栏
    private NavigationView navView; // 导航视图
    private MapView mapView; // 地图视图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 隐私协议
//        MapsInitializer.updatePrivacyAgree(this,true);
//        MapsInitializer.updatePrivacyShow(this,true,true);

        // 初始化组件
        initViews();

        // 设置Toolbar作为ActionBar
        setSupportActionBar(toolbar);
        // 注意：如果你的应用目标API级别是Android Lollipop（API 21）或更高，
        // 并且你想要在Toolbar上显示导航图标（如汉堡图标），你可能还需要调用以下代码：
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // 替换为你的汉堡菜单图标资源
        // 高德地图SDK初始化之前调用

        // 初始化地图
        mapView.onCreate(savedInstanceState); // 必须调用
        AMap aMap = mapView.getMap(); // 获取地图控制器对象

        // 设置导航视图头部布局点击事件
        View headerView = navView.getHeaderView(0);
        headerView.setOnClickListener(view -> showMsg("点击了头部布局"));
        // 设置导航视图菜单点击事件
        navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.item_order) {
                showMsg("点击了我的订单");
            } else if (id == R.id.item_wallet) {
                showMsg("点击了我的钱包");
            } else if (id == R.id.item_guidance) {
                showMsg("点击了使用指南");
            }
            // 点击任何项之后关闭抽屉
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // 设置工具栏按钮点击事件，打开抽屉
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);
        mapView = (MapView)findViewById(R.id.map);
    }

    /**
     * Toast提示
     * @param msg 内容
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
