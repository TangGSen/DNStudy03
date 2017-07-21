package com.sen.httpservice.http.interfaces;

import org.apache.http.HttpEntity;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/12.
 */

public interface IHttpListener {
    /**
     * 处理结果  成功回调
     * @param httpEntity
     */
    void onSuccess(HttpEntity httpEntity);

    /**
     * 失败回调
     */
    void onFail();

    /**
     * 添加请求头的方法
     */
    void addHttpHead(Map<String,String> map);
}
