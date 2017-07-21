package com.sen.httpservice.http;

import com.sen.httpservice.http.interfaces.IDataListener;
import com.sen.httpservice.http.interfaces.IHttpListener;
import com.sen.httpservice.http.interfaces.IHttpService;

/**
 * Created by Administrator on 2017/7/12.
 */

public class SenHttp {
    public  static <T,M> void sendRequest(T requestInfo, String url, Class<M> repsonece, IDataListener dataListener){
        RequestHodler<T> requestHodler = new RequestHodler<>();
        requestHodler.setUrl(url);
        requestHodler.setRequestInfo(requestInfo);
        IHttpService httpService = new JsonHttpService();
        IHttpListener httpListener = new JasonDealListener<>(repsonece,dataListener);
        requestHodler.setHttpService(httpService);
        requestHodler.setHttpListener(httpListener);
        HttpTask<T> httpTask = new HttpTask<>(requestHodler);
        try {
            ThreadPoolManager.getInstace().execute(httpTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
            httpListener.onFail();
        }
    }
}
