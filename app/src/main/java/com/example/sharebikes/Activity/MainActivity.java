package com.example.sharebikes.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.sharebikes.BmobDatabase.Bike;
import com.example.sharebikes.BmobDatabase.Station;
import com.example.sharebikes.BmobDatabase.User;
import com.example.sharebikes.R;
import com.amap.api.maps.MapView;
import com.google.android.material.navigation.NavigationView;
import com.king.camera.scan.CameraScan;

import java.io.File;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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
    private Toast toast;
    public String defaultAvatarUrl = null;
    public static final int REQUEST_CODE_SCAN = 0x01;
    String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MapsInitializer.updatePrivacyShow(this,true,true);
//        MapsInitializer.updatePrivacyAgree(this,true);
        setContentView(R.layout.activity_main);
        // 获取objectId
        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
        Log.d("MainActivity", "获取到了objectId: " + objectId);
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
                Intent intent1 = new Intent(MainActivity.this, UserActivity.class);
                intent1.putExtra("objectId", objectId); // 使用putExtra方法传递数据
                startActivity(intent1);
                finish(); // Close the activity after successful registration
            } else if (id == R.id.item_order) {
                showMsg("点击了我的订单");
            } else if (id == R.id.item_wallet) {
                showMsg("点击了我的钱包");
            } else if (id == R.id.item_guidance) {
                showMsg("点击了使用指南");
            } else if (id == R.id.item_navigation) {
                showMsg("进入导航界面");
                Intent intent1 = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent1);
                finish();
            }else if(id == R.id.item_station){
                showMsg("进入租赁点界面");
                Intent intent1 = new Intent(MainActivity.this, StationActivity.class);
                startActivity(intent1);
                finish();
            }
            // 点击任何项之后关闭抽屉
            drawerLayout.closeDrawer( GravityCompat.START);
            return true;
        });

        // 设置工具栏按钮点击事件，打开抽屉
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        // 设置扫码按钮点击事件运行scan方法
        scanButton.setOnClickListener(view -> startScan());
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
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                // 当定位改变时，生成租赁点
                //generateBikePoints(location);
            }
        });
        initStation();
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
    private void startScan() {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.in, R.anim.out);
        Intent intent = new Intent(this, QRCodeScanActivity.class);
        ActivityCompat.startActivityForResult(this, intent, REQUEST_CODE_SCAN, optionsCompat.toBundle());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String result = CameraScan.parseScanResult(data);
            showToast(result);
        }
    }

    private void showToast(String text) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, String.valueOf(text), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void addBike(){
        // 创建一个新的Bike对象
        Bike bike = new Bike();
        bike.setBikeName("BK00001");
        bike.setIsReturn(1);
        bike.setStationId("1588829eea");
        bike.setObjectId("00001");  // 这里你可以设置实际的objectId
        // 将Bike对象保存到Bmob数据库
        bike.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Log.d("BikeRentalPoint", "自行车保存成功，objectId：" + objectId);
                } else {
                    Log.e("BikeRentalPoint", "自行车保存失败：" + e.getMessage());
                }
            }
        });
    }

    private void generateBikePoints(Location location) {
        Log.d("BikeRentalPoint", "开始生成租赁点");
        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        Random random = new Random();

        int numberOfPoints = 5;  // 生成5个点
        double RADIUS = 0.01;    // 大约1公里范围

        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_bikestation);  // 加载自定义图标

        for (int i = 0; i < numberOfPoints; i++) {
            double offsetLat = (random.nextDouble() - 0.5) * 2 * RADIUS;
            double offsetLng = (random.nextDouble() - 0.5) * 2 * RADIUS;

            LatLng randomLatLng = new LatLng(currentLocation.latitude + offsetLat, currentLocation.longitude + offsetLng);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(randomLatLng)
                    .title("租赁点 " + (i + 1))
                    .icon(icon);  // 使用自定义图标
            aMap.addMarker(markerOptions);

            // 使用Log.d记录坐标
            Log.d("BikeRentalPoint", "Point " + (i + 1) + ": (" + randomLatLng.latitude + ", " + randomLatLng.longitude + ")");
        }
    }

    public void addStation() {
        // 生成租赁点
        // 创建一个新的Station对象
        Station station = new Station();
        station.setStationName("租赁点1");
        station.setStationAddress("江苏省苏州市姑苏区双塔街道庄先湾6号"); // 这里你可以设置实际的地址
        station.setBikeNum(10); // 这里你可以设置实际的自行车数量
        station.setLatitude(120.644504);
        station.setLongitude(31.302579);
        station.setPhone("18275464058"); // 这里你可以设置实际的联系电话

        // 将Station对象保存到Bmob数据库
        station.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Log.d("BikeRentalPoint", "租赁点保存成功，objectId：" + objectId);
                } else {
                    Log.e("BikeRentalPoint", "租赁点保存失败：" + e.getMessage());
                }
            }
        });
    }


    private void initStation(){
        // 创建BmobQuery对象
        BmobQuery<Station> query = new BmobQuery<>();
// 执行查询操作
        query.findObjects(new FindListener<Station>() {
            @Override
            public void done(List<Station> object, BmobException e) {
                Bitmap originalIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.pk_bike);
                Bitmap scaledIcon = Bitmap.createScaledBitmap(originalIcon, 70, 70, false);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(scaledIcon);
                if(icon == null){
                    Log.d("Bmob", "icon is null");
                }
                if (e == null) {
                    // 移除地图上的所有标记
                    aMap.clear();
                    // 遍历查询结果
                    for (Station station : object) {
                        // 获取经纬度
                        LatLng latLng = new LatLng(station.getLatitude(), station.getLongitude());
                        // 创建MarkerOptions对象
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(latLng)
                                .title(station.getStationName())
                                .icon(icon)  // 设置自定义图标
                                .anchor(0.5f, 0.5f)  // 设置锚点
                                .perspective(true);  // 开启近大远小效果
                        // 在地图上添加标记
                        aMap.addMarker(markerOptions);
                    }
                } else {
                    Log.e("Bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}

