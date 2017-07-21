package com.sen.imageloaderframwork.request;

import android.util.Log;

import com.sen.imageloaderframwork.loader.Loader;
import com.sen.imageloaderframwork.loader.LoaderManager;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Administrator on 2017/7/18.
 * 转发器
 * 不断的从RequestQueue 请求队列中获取请求
 */

public class RequestDispatcher extends Thread {
    private BlockingQueue<BitmapRequest> mRequestQue;

    public RequestDispatcher(BlockingQueue<BitmapRequest> blockingDeque) {
        this.mRequestQue = blockingDeque;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                BitmapRequest bitmapRequest = mRequestQue.take();
                //处理请求对象
                String schema = parseSchema(bitmapRequest.getImagUrl());
                Loader loader = LoaderManager.getInstance().getLoader(schema);
                loader.loadImage(bitmapRequest);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private String parseSchema(String imagUrl) {
        if (imagUrl.contains("://")){
            //获取http https file 前面部分就好了，
            return imagUrl.split("://")[0];
        }else{
            Log.i("RequestDispatcher","不支持的格式,以后会拓展R.id ，drawable这种");
        }
        return null;
    }
}
