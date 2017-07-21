package com.sen.httpservice.http;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSONObject;
import com.sen.httpservice.http.interfaces.IDataListener;
import com.sen.httpservice.http.interfaces.IHttpListener;

import org.apache.http.HttpEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 *
 * T 对应的是响应类
 * Created by Administrator on 2017/7/12.
 */

public class JasonDealListener<M> implements IHttpListener {
    private Class<M> reponese ;
    private IDataListener<M> dataListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    public JasonDealListener(Class<M> reponese, IDataListener<M> dataListener) {
        this.reponese = reponese;
        this.dataListener = dataListener;
    }

    @Override
    public void onSuccess(HttpEntity httpEntity) {
        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();

            /**
             * 得到网络返回的数据
             * 子线程
             */
            String content = getContent(inputStream);
            final M m = JSONObject.parseObject(content,reponese);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    dataListener.onSuccess(m);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            dataListener.onFail();
        }

    }

    private String getContent(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while((line = reader.readLine())!=null){
                sb.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            dataListener.onFail();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    @Override
    public void onFail() {
        //框架层出错后调用，调用层的onfail
        dataListener.onFail();
    }

    @Override
    public void addHttpHead(Map<String, String> map) {

    }
}
