package com.example.sharebikes.BmobDatabase;

import cn.bmob.v3.BmobObject;

public class Station extends BmobObject {
    private String stationName; // 站点名称
    private String stationAddress; // 站点地址
    private int bikeNum; // 自行车数量
    private Double latitude; // 纬度
    private Double longitude; // 经度
    private String phone; // 联系电话

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public int getBikeNum() {
        return bikeNum;
    }

    public void setBikeNum(int bikeNum) {
        this.bikeNum = bikeNum;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
