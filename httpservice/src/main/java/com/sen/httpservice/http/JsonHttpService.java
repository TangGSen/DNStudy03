package com.sen.httpservice.http;

import com.sen.httpservice.http.interfaces.IHttpListener;
import com.sen.httpservice.http.interfaces.IHttpService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/12.
 */

public class JsonHttpService implements IHttpService {
    private String mUrl;
    private IHttpListener mHttpListener;
    private byte[] mRequestData;

    HttpClient mHttpClient = new DefaultHttpClient();
    private HttpPost mHttpPost;
    private HttpResponeseHandler responeseHandler = new HttpResponeseHandler();

    @Override
    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public void excucte() {
        mHttpPost = new HttpPost(mUrl);
        ByteArrayEntity entity = new ByteArrayEntity(mRequestData);
        mHttpPost.setEntity(entity);
        try {
            mHttpClient.execute(mHttpPost,responeseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            mHttpListener.onFail();
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
            if (code ==200){
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

    }

    @Override
    public Map<String, String> getHeaderMap() {
        return null;
    }

    @Override
    public boolean isPause() {
        return false;
    }
}
