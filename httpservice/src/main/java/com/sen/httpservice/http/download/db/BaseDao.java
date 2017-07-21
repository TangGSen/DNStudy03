package com.sen.httpservice.http.download.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.sen.httpservice.http.download.db.annotation.DbField;
import com.sen.httpservice.http.download.db.annotation.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/7/9.
 *
 */

public abstract class BaseDao<T> implements IBaseDao<T> {
    //protected 为了给子类来调用
    protected SQLiteDatabase mSQlDb;
    //保证实例化一次
    private boolean isInit = false;
    private String mTableName;
    /**
     * 持有数据库表所对应的java类型
     */
    private Class<T> clazz;
    /**
     * 表的字段与成员变量的关系映射map
     * key - 表字段
     * value 成员变量
     */
    private Map<String, Field> mCashMap = new HashMap<>();
    private Set keys;

    /**
     * 注意： 架构设计时，只有暴露出去才使用public
     * y映射表只初始化一次实例
     * <p>
     * BaseDao 是以数据库底层打交道，所以初始化时需要Sqlitedabase 的引用
     *
     * @return
     */
    protected synchronized boolean init(Class<T> clazz, SQLiteDatabase database) {
        if (!isInit) {
            mSQlDb = database;

            this.clazz = clazz;

            if (clazz.getAnnotation(DbTable.class) == null) {
                //假如使用该框架者没有@DbTable 所遵守你的框架
                mTableName = clazz.getClass().getSimpleName();

            } else {
                mTableName = clazz.getAnnotation(DbTable.class).value();
                if (TextUtils.isEmpty(mTableName))
                    mTableName = clazz.getClass().getSimpleName();
            }

            if (!database.isOpen())
                return false;

            //创建表
            String createTableSQL = createTableSQL();
            if (!TextUtils.isEmpty(createTableSQL)){
                database.execSQL(createTableSQL);
            }
            //初始化映射表
            initCashMap();
            isInit = true;
        }
        return isInit;
    }

    /**
     * 创建表
     * @return
     */
    public abstract String createTableSQL();

    private void initCashMap() {
        //不管有没有数据，先查询一次，为了得到表的字段
        String sql = "select * from " + this.mTableName + " limit 1 , 0";
        Cursor cursor = null;
        try {
            cursor = mSQlDb.rawQuery(sql, null);

            /**
             * 表的字段数组
             */
            String[] columnNames = cursor.getColumnNames();

            /**
             * 拿到Filed 数组
             */
            Field[] colmunFileds = clazz.getFields();

            /**
             * 开始找对应关系
             */
            for (String columnName : columnNames) {
                Field colmunFiled = null;
                for (Field field : colmunFileds) {
                    field.setAccessible(true);
                    String filedName = null;
                    if (field.getAnnotation(DbField.class) != null) {
                        filedName = field.getAnnotation(DbField.class).value();
                    } else {
                        filedName = field.getName();
                    }
                    //如果表的字段等于了成员变量
                    if (columnName.equals(filedName)) {
                        colmunFiled = field;
                        //跳出里面的循环
                        break;
                    }
                }
                //找到了对应的关系
                if (colmunFiled != null)
                    mCashMap.put(columnName, colmunFiled);

            }
        } catch (Exception e) {
            System.out.print("");
            Log.e("sen",e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }

    }



    private ContentValues getContentValues(Map<String, String> resultMap) {
        ContentValues values = new ContentValues();

        Set keys = resultMap.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = resultMap.get(key);
            values.put(key,value);
        }
        return values;
    }

    private Map<String, String> getValues(T entity) {
        HashMap<String, String> values = new HashMap<>();
        //遍历映射表Filed
        Iterator<Field> iter = mCashMap.values().iterator();
        while (iter.hasNext()){
            Field  columnedField = iter.next();
            String cachKey = null;
            String cachValue = null;
            if (columnedField.getAnnotation(DbField.class)!=null){
                cachKey = columnedField.getAnnotation(DbField.class).value();
            }else{
                cachKey = columnedField.getName();
            }

            try {
                if (columnedField.get(entity)==null){
                    continue ;
                }
                cachValue = columnedField.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            values.put(cachKey,cachValue);
        }
        return values;
    }

    /**
     * 增
     * @param entity
     * @return
     */
    @Override
    public long instert(T entity) {
        //首先将传进来的实体类，先跟关系表，产生一个结果的map,再生产ContentValue
        Map<String, String> resultMap = getValues(entity);
        ContentValues values = getContentValues(resultMap);
        return mSQlDb.insert(mTableName,null,values);
    }

    /**
     * 改
     * @param entity
     * @param where
     * @return
     */
    @Override
    public int update(T entity, T where) {
        Map values = getValues(entity);
        Map whereClause = getValues(where);
        Condition condition = new Condition(whereClause);
        ContentValues contentValues = getContentValues(values);
        return  mSQlDb.update(mTableName,contentValues,condition.getWhereCondition(),condition.getWhereArgs());
    }

    /**
     * 删
     * @param where
     * @return
     */
    @Override
    public int delete(T where) {
        Map values = getValues(where);
        Condition condition = new Condition(values);

        return  mSQlDb.delete(mTableName,condition.getWhereCondition(),condition.getWhereArgs());
    }

    /**
     * 查
     * @param where
     * @return
     */
    @Override
    public List<T> query(T where) {
        return query(where,null,null,null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        Map map = getValues(where);
        String limits = null;
        if (startIndex !=null && limit !=null){
            limits = startIndex+" , "+ limit;
        }
        Condition condition = new Condition(map);
        Cursor cursor = mSQlDb.query(mTableName ,null,
                condition.getWhereCondition(),condition.getWhereArgs(),null,null,orderBy,limits);
        List<T> result = getQueryResult(cursor ,where);
        cursor.close();
        return result;
    }

    /**
     * 根据CashMap 保存的是，key 为表的列名，value 为成员变量
     * @param cursor
     * @param where
     * @return
     */
    private List<T> getQueryResult(Cursor cursor, T where) {
        List<T> list = new ArrayList<>();
        T item ;
        while (cursor.moveToNext()){
            try {
                item = (T) where.getClass().newInstance();
                Iterator iterator = mCashMap.entrySet().iterator();
                while (iterator.hasNext()){
                    Map.Entry entry = (Map.Entry) iterator.next();
                    //得到列名
                    String colmnunName = (String) entry.getKey();
                    //然后以列名，拿到cursour 中的位置
                    Integer colmmIndex = cursor.getColumnIndex(colmnunName);

                    Field field = (Field) entry.getValue();
                    //根据类型
                    Class type = field.getType();
                    if (colmmIndex!=-1){
                        if (type == String.class){
                            field.set(item ,cursor.getString(colmmIndex));
                        }else if (type == Double.class){
                            field.set(item ,cursor.getDouble(colmmIndex));
                        }else if (type == Integer.class){
                            field.set(item ,cursor.getInt(colmmIndex));
                        }else if (type == Long.class){
                            field.set(item ,cursor.getLong(colmmIndex));
                        }else if (type == Boolean.class){
                            field.set(item ,cursor.getBlob(colmmIndex));
                        }else if (type == Short.class){
                            field.set(item ,cursor.getShort(colmmIndex));
                        }else{
                            //不支持的类型
                            continue;
                        }
                    }

                }
                list.add(item);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    class Condition{
        private String whereCondition;
        private String[] whereArgs;

        public Condition(Map<String,String> whereClause) {
            StringBuffer buffer = new StringBuffer();
            ArrayList<String> list = new ArrayList();
//            where 1=1 是为了避免where 关键字后面的第一个词直接就是 “and”而导致语法错误。
            buffer.append(" 1=1 ");
            Set keys = whereClause.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                String value = whereClause.get(key);
                if (value != null){
                    buffer.append(" and "+key +" =?");
                    list.add(value);
                }
            }
            this.whereCondition = buffer.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }


        public String getWhereCondition() {
            return whereCondition;
        }

        public void setWhereCondition(String whereCondition) {
            this.whereCondition = whereCondition;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }

        public void setWhereArgs(String[] whereArgs) {
            this.whereArgs = whereArgs;
        }
    }

    public String getTableName(){
        return mTableName;
    }


}
