package com.example.sharebikes.BmobDatabase;

import android.provider.ContactsContract;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Rent_order extends BmobObject {
    private String userId; // 用户ID
    private String bikeId; // 自行车ID
    private Double cost; // 花费
    private BmobDate startTime; // 开始时间
    private BmobDate endTime; // 结束时间

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getBikeId() {
        return bikeId;
    }
    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }
    public Double getCost() {
        return cost;
    }
    public void setCost(Double cost) {
        this.cost = cost;
    }
    public BmobDate getStartTime() {
        return startTime;
    }
    public void setStartTime(BmobDate startTime) {
        this.startTime = startTime;
    }
    public BmobDate getEndTime() {
        return endTime;
    }
    public void setEndTime(BmobDate endTime) {
        this.endTime = endTime;
    }

}
