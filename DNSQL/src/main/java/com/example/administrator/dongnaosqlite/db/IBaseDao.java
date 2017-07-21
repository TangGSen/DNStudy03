package com.example.administrator.dongnaosqlite.db;

/**
 * Created by Administrator on 2017/1/9 0009.
 */

public interface IBaseDao<T> {
    /**
     * 插入数据
     * @param entity
     * @return
     */
    Long insert(T entity);

    /**
     *
     * @param entity
     * @param where
     * @return
     */
    Long  update(T entity,T where);
}
