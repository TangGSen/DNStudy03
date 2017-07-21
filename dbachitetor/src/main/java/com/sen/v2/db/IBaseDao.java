package com.sen.v2.db;

import java.util.List;

/**
 * Created by Administrator on 2017/7/9.
 */

public interface IBaseDao<T> {
    /**
     * 要插入的对象
     * @param entity
     * @return
     */
    long instert(T entity);

    /**
     *
     * @return
     */
    int update(T entity, T where);

    int delete(T where);

    List<T> query(T where);

    List<T> query(T where, String orderBy, Integer startIndex, Integer limit);

}
