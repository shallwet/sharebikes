package com.example.sharebikes.BmobDatabase;

import cn.bmob.v3.BmobObject;

public class Bike extends BmobObject {
    private String bikeName; // 自行车名称
    private String stationId; // 站点ID
    private int isReturn; // 是否归还
    public String getBikeName() {
        return bikeName;
    }
    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }
    public String getStationId() {
        return stationId;
    }
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
    public int getIsReturn() {
        return isReturn;
    }
    public void setIsReturn(int isReturn) {
        this.isReturn = isReturn;
    }
}
