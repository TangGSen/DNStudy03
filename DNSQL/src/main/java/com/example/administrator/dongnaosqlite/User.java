package com.example.administrator.dongnaosqlite;

import com.example.administrator.dongnaosqlite.db.annotion.DbFiled;
import com.example.administrator.dongnaosqlite.db.annotion.DbTable;

/**
 * Created by Administrator on 2017/1/9 0009.
 */
@DbTable("tb_user")
public class User {
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User( ) {
    }

    @DbFiled("name")
    public String name;
    //123456
    @DbFiled("password")
    public String password;
}
