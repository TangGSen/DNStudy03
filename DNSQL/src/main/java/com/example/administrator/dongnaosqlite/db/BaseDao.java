package com.example.administrator.dongnaosqlite.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.administrator.dongnaosqlite.db.annotion.DbFiled;
import com.example.administrator.dongnaosqlite.db.annotion.DbTable;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

public abstract class BaseDao<T> implements  IBaseDao<T> {
    /**]
     * 持有数据库操作类的引用
     */
    private SQLiteDatabase database;
    /**
     * 保证实例化一次
     */
    private boolean isInit=false;
    /**
     * 持有操作数据库表所对应的java类型
     * User
     */
    private Class<T> entityClass;
    /**
     * 维护这表名与成员变量名的映射关系
     * key---》表名
     * value --》Field
     */
    private HashMap<String,Field> cacheMap;

    private String tableName;
    /**
     * @param entity
     * @param sqLiteDatabase
     * @return
     * 实例化一次
     */
    protected synchronized boolean init(Class<T> entity, SQLiteDatabase sqLiteDatabase)
    {
        if(!isInit)
        {
            entityClass=entity;
            database=sqLiteDatabase;
            if (entity.getAnnotation(DbTable.class)==null)
            {
               tableName=entity.getClass().getSimpleName();
            }else
            {
                tableName=entity.getAnnotation(DbTable.class).value();
            }
              if(!database.isOpen())
              {
                  return  false;
              }
                if(!TextUtils.isEmpty(createTable()))
                {
                    database.execSQL(createTable());
                }
              cacheMap=new HashMap<>();
              initCacheMap();

            isInit=true;
        }
        return  isInit;
    }

    /**
     * 维护映射关系
     */
    private void initCacheMap() {
        String sql="select * from "+this.tableName+" limit 1 , 0";
        Cursor cursor=null;
        try {
            cursor=database.rawQuery(sql,null);
            /**
             * 表的列名数组
             */
            String[] columnNames=cursor.getColumnNames();
            /**
             * 拿到Filed数组
             */
            Field[] colmunFields=entityClass.getFields();
            for(Field filed:colmunFields)
            {
                 filed.setAccessible(true);
            }
            /**
             * 开始找对应关系
             */
            for(String colmunName:columnNames)
            {
                /**
                 * 如果找到对应的Filed就赋值给他
                 * User
                 */
                 Field colmunFiled=null;
                for (Field field:colmunFields)
                {
                    String fieldName=null;
                    if(field.getAnnotation(DbFiled.class)!=null)
                    {
                         fieldName=field.getAnnotation(DbFiled.class).value();
                    }else
                    {
                        fieldName =field.getName();
                    }
                    /**
                     * 如果表的列名 等于了  成员变量的注解名字
                     */
                    if(colmunName.equals(fieldName))
                    {
                        colmunFiled= field;
                        break;
                    }
                }
                //找到了对应关系
                if(colmunFiled!=null)
                {
                    cacheMap.put(colmunName,colmunFiled);
                }
            }

        }catch (Exception e)
        {

        }finally {
            cursor.close();
        }

    }

    @Override
    public Long insert(T entity) {
        Map<String,String> map=getValues(entity);
        ContentValues values=getContentValues(map);
        Long result =database.insert(tableName,null,values);
        return result;
    }
    /**
     * 讲map 转换成ContentValues
     * @param map
     * @return
     */
    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues=new ContentValues();
        Set keys=map.keySet();
        Iterator<String> iterator=keys.iterator();
        while (iterator.hasNext())
        {
            String key=iterator.next();
            String value=map.get(key);
            if(value!=null)
            {
                contentValues.put(key,value);
            }
        }

       return contentValues;
    }

    private Map<String, String> getValues(T entity) {
        HashMap<String,String> result=new HashMap<>();
        Iterator<Field> filedsIterator=cacheMap.values().iterator();
        /**
         * 循环遍历 映射map的  Filed
         */
        while (filedsIterator.hasNext())
        {
            /**
             *
             */
            Field colmunToFiled=filedsIterator.next();
            String cacheKey=null;
            String cacheValue=null;
            if(colmunToFiled.getAnnotation(DbFiled.class)!=null)
            {
                cacheKey=colmunToFiled.getAnnotation(DbFiled.class).value();
            }else
            {
                cacheKey=colmunToFiled.getName();
            }
            try {
                if(null==colmunToFiled.get(entity))
                {
                    continue;
                }
                cacheValue=colmunToFiled.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(cacheKey,cacheValue);
        }

        return result;
    }

    @Override
    public Long update(T entity, T where) {
        return null;
    }

    /**
     * 创建表
     * @return
     */
    protected  abstract  String createTable();
}
