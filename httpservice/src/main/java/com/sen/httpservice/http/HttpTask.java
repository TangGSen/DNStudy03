package com.sen.httpservice.http;

import com.alibaba.fastjson.JSON;
import com.sen.httpservice.http.interfaces.IHttpListener;
import com.sen.httpservice.http.interfaces.IHttpService;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2017/7/12.
 */

public class HttpTask<T> implements Runnable {
    private IHttpService mHttpService;
    private FutureTask futureTask;

    public HttpTask(RequestHodler<T> requestHodler) {
        this.mHttpService = requestHodler.getHttpService();
        mHttpService.setHttpListener(requestHodler.getHttpListener());
        mHttpService.setUrl(requestHodler.getUrl());
        IHttpListener httpListener = requestHodler.getHttpListener();
        httpListener.addHttpHead(mHttpService.getHeaderMap());
        try {
            T t = requestHodler.getRequestInfo();
            if (t!=null) {
                String request = JSON.toJSONString(t);
                mHttpService.setRequestData(request.getBytes("UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        mHttpService.excucte();
    }
    /**
     * 新增方法
     */
    public void start()
    {
        futureTask=new FutureTask(this,null);
        try {
            ThreadPoolManager.getInstace().execute(futureTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 新增方法
     */
    public  void pause()
    {
        mHttpService.pause();
        if(futureTask!=null)
        {
            ThreadPoolManager.getInstace().removeTask(futureTask);
        }

    }
}
