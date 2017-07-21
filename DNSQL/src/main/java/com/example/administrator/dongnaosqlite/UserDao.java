package com.example.administrator.dongnaosqlite;

import com.example.administrator.dongnaosqlite.db.BaseDao;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

public class UserDao extends BaseDao {
    @Override
    protected String createTable() {
        return "create table if not exists tb_user(name varchar(20),password varchar(10))";
    }
}
