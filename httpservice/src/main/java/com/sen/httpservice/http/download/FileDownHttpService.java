package com.sen.httpservice.http.download;

import android.util.Log;

import com.sen.httpservice.http.interfaces.IHttpListener;
import com.sen.httpservice.http.interfaces.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2017/7/13.
 */

public class FileDownHttpService implements IHttpService {
    private static String TAG = "sen";
    private String mUrl;
    private IHttpListener mHttpListener;
    private byte[] mRequestData;
    //增加方法
    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    HttpClient mHttpClient = new DefaultHttpClient();
    private HttpGet mHttpPost;

    private HttpResponeseHandler responeseHandler = new HttpResponeseHandler();
    /**
     * 将请求头 的信息
     * 需要线程安全，因为多个线程使用
     */
    private Map<String,String> mHeadMap = Collections.synchronizedMap(new HashMap<String, String>());

    @Override
    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public void excucte() {
        mHttpPost = new HttpGet(mUrl);
        createHeader();

//        ByteArrayEntity entity = new ByteArrayEntity(mRequestData);
//        mHttpPost.setEntity(entity);
        try {
            mHttpClient.execute(mHttpPost,responeseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            mHttpListener.onFail();
        }
    }

    private void createHeader() {
        //mHeadMap 数据来源于IDownloadListener
        Iterator<String> iterator = mHeadMap.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = mHeadMap.get(key);
            mHttpPost.addHeader(key,value);
            Log.e(TAG,"请求头信息：key="+key+" ，value ="+value);
        }
    }

    @Override
    public void setHttpListener(IHttpListener httpListener) {
        this.mHttpListener = httpListener;
    }

    @Override
    public void setRequestData(byte[] requestData) {
        this.mRequestData = requestData;
    }

    private class HttpResponeseHandler extends BasicResponseHandler {
        @Override
        public String handleResponse(HttpResponse response) throws ClientProtocolException {
            int code = response.getStatusLine().getStatusCode();
            //断点下载时是206
            if (code ==200 || code ==206){
                mHttpListener.onSuccess(response.getEntity());
            }else {
                mHttpListener.onFail();
            }
            return null;
        }
    }

    @Override
    public boolean cancle() {
        return false;
    }

    @Override
    public boolean isCancle() {
        return false;
    }

    @Override
    public void pause() {
        atomicBoolean.compareAndSet(false,true);
    }

    @Override
    public boolean isPause() {
        return atomicBoolean.get();
    }

    @Override
    public Map<String, String> getHeaderMap() {
        return mHeadMap;
    }
}
