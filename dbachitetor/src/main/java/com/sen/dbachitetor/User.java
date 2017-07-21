package com.sen.dbachitetor;


import com.sen.v2.db.annotation.DbField;
import com.sen.v2.db.annotation.DbTable;

/**
 * Created by Administrator on 2017/7/9.
 */
@DbTable("tb_user")
public class User {
    @DbField("name")
    public String name;
    @DbField("password")
    public String password;
    @DbField("user_id")
    public String userId;
    @DbField("status")
    public Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", userId=" + userId +
                '}';
    }
}
