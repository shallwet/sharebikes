package com.example.sharebikes;

import android.app.Application;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.navi.NaviSetting;

import cn.bmob.v3.Bmob;


public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "df96a26480722f9b861480553b1ee062");
        MapsInitializer.updatePrivacyShow(this,true,true);
        MapsInitializer.updatePrivacyAgree(this,true);
        NaviSetting.updatePrivacyShow(this, true, true);
        NaviSetting.updatePrivacyAgree(this, true);
    }
}

