package com.example.sharebikes.BmobDatabase;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobObject {
    private String username; // 用户名
    private String password; // 密码
    private String phonenumber; // 手机号
    private String sex; // 性别
    private Double balance; // 余额
    private String avatar;  // 添加头像字段

    // 构造函数，getter 和 setter 省略...

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

}
