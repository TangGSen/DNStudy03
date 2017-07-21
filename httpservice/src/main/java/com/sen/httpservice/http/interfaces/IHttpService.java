package com.sen.httpservice.http.interfaces;

import java.util.Map;

/**
 * 获取网络数据
 * Created by Administrator on 2017/7/12.
 */

public interface IHttpService {
    void setUrl(String url);

    /**
     * 执行获取网络数据
     */
    void excucte();

    /**
     * 设置回调结果监听
     * @param httpListener
     */
    void setHttpListener(IHttpListener httpListener);

    /**
     * 设置请求参数
     * @param requestData
     */
    void setRequestData(byte[] requestData);

    /**
     * 以下方法额外的
     */

    void pause();

    Map<String ,String > getHeaderMap();

    boolean cancle();

    boolean isCancle();

    boolean isPause();
}
