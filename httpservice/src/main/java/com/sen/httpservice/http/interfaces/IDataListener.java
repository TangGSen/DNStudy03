package com.sen.httpservice.http.interfaces;

/**
 * 回调结果给调用层，需要泛型
 * Created by Administrator on 2017/7/12.
 */

public interface IDataListener<M> {

    void onSuccess(M m);

    void onFail();

}
