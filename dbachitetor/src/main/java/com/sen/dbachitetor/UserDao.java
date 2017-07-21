package com.sen.dbachitetor;

import android.util.Log;

import com.sen.v2.db.BaseDao;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */

public class UserDao extends BaseDao<User> {
    @Override
    public String createTableSQL() {
        return "create table if not exists tb_user( name TEXT, password TEXT, user_id Text,status Integer);";
    }
    //得到当前登陆的用户
    public User getCurrentUser() {
        User where = new User();
        where.setStatus(1);
        List<User> users= super.query(where);
        if (users.size()>0){
            return users.get(0);
        }
        return null;
    }

    //重写父类方法

    @Override
    public long instert(User entity) {
        //先把不是当前用户的先置于0
        Long result = -1L;
        List<User> allUser = query(new User());
        User where = null;
        boolean isCurrentHas= false;
        for (User user:allUser){
            where = new User();
            where.setUserId(user.getUserId());
           if (user.getUserId().equals(entity.getUserId())){
               isCurrentHas = true;
               result = 1L;
               user.setStatus(1);
           }else{
               user.setStatus(0);
           }
            Log.e("sen","用户："+user.getUserId()+"：状态为"+user.getStatus());
           update(user,where);
        }

        if (!isCurrentHas){
            result=  super.instert(entity);
            Log.e("sen","没这个用户");
        }else{
            Log.e("sen","有这个用户");
        }
        return result;
    }
}
