package com.sen.v2.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.sen.v2.db.emuns.PrivateDataBaseEnmus;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 由于有很多对象都要保存，比如有UserDao. fileDao,...
 * 使用简单工厂来生产
 * Created by Administrator on 2017/7/9.
 */

public class BaseDaoFactory {
    /**
     * 数据库保存在sd ,一些大公司都是这么做，这样就保持了用户第二次安装都存在数据
     *
     */
    private String sqliteDataPath;

    private SQLiteDatabase mSQliteDatabase;
    /**
     * 多用户登陆使用的
     */

    private Map<String ,BaseDao> map = Collections.synchronizedMap(new HashMap<String, BaseDao>());

    private SQLiteDatabase mUserDatabase;
    private static BaseDaoFactory mInstance;

    private BaseDaoFactory(){
        File file = new File(Environment.getExternalStorageDirectory(),"update");
        if (!file.exists()){
            file.mkdir();
        }
        sqliteDataPath = file.getAbsolutePath()+File.separator+"user.db";
        openDatabase();
    }

    public static BaseDaoFactory getInstace(){
        if (mInstance==null){
            synchronized (BaseDaoFactory.class){
                if (mInstance ==null)
                    mInstance= new BaseDaoFactory();
            }
        }
        return mInstance;
    }
    // T 代表的是 userDao ,m 代表的是user
    public synchronized <T extends BaseDao<M>,M> T getDataHelper(Class<T> clazz, Class<M> entityClass){
        BaseDao baseDao = null;
        if (map.get(clazz.getSimpleName())!=null){
            return (T) map.get(clazz.getSimpleName());
        }
        try {
            baseDao = clazz.newInstance();
            //初始化，数据库表以user 保持映射，在BaseDao 初始化的
            baseDao.init(entityClass ,mSQliteDatabase);
            map.put(clazz.getSimpleName(),baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }


    // T 代表的是 userDao ,m 代表的是user
    public synchronized <T extends BaseDao<M>,M> T getUserHelper(Class<T> clazz, Class<M> entityClass){
        BaseDao baseDao = null;
        String userDataPath = PrivateDataBaseEnmus.database.getValue();
        mUserDatabase =  SQLiteDatabase.openOrCreateDatabase(userDataPath,null);
        if (map.get(clazz.getSimpleName())!=null){
            return (T) map.get(clazz.getSimpleName());
        }
        try {
            baseDao = clazz.newInstance();
            //初始化，数据库表以user 保持映射，在BaseDao 初始化的
            baseDao.init(entityClass ,mUserDatabase);
            map.put(clazz.getSimpleName(),baseDao);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }

    private void openDatabase() {
        mSQliteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDataPath ,null);

    }
}
