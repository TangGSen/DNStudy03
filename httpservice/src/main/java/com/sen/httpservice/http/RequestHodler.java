package com.sen.httpservice.http;

import com.sen.httpservice.http.interfaces.IHttpListener;
import com.sen.httpservice.http.interfaces.IHttpService;

/**
 * Created by Administrator on 2017/7/12.
 */

public class RequestHodler <T>{
    /**
     * 执行网络类
     */
    private IHttpService httpService;
    private String url;
    /**
     * 获取网络数据，回调结果
     */
    private IHttpListener httpListener;
    /**
     * 请求网络，对应的回调结果实体
     */
    private T requestInfo;

    public IHttpService getHttpService() {
        return httpService;
    }

    public void setHttpService(IHttpService httpService) {
        this.httpService = httpService;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IHttpListener getHttpListener() {
        return httpListener;
    }

    public void setHttpListener(IHttpListener httpListener) {
        this.httpListener = httpListener;
    }

    public T getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(T requestInfo) {
        this.requestInfo = requestInfo;
    }
}
