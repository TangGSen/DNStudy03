package com.sen.imageloaderframwork.loader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.sen.imageloaderframwork.config.DisplayConfig;
import com.sen.imageloaderframwork.config.ImageLoaderConfig;
import com.sen.imageloaderframwork.request.BitmapRequest;
import com.sen.imageloaderframwork.request.RequestQueue;

/**
 * Created by Administrator on 2017/7/18.
 * 单例模式
 */

public class SimpleImageloader {
    //需要拿到配置
    private ImageLoaderConfig imageoaderConfig;
    //请求队列
    private RequestQueue requestQueue;

    //单例

    private static volatile SimpleImageloader mInstance;

    private SimpleImageloader() {

    }

    private SimpleImageloader(ImageLoaderConfig config) {
        this.imageoaderConfig = config;
        requestQueue = new RequestQueue(config.getDefThreadCount());
        requestQueue.start();
    }

    /**
     * 第一次初始化，是在Application 或者Activity
     *
     * @param config
     * @return
     */
    public static SimpleImageloader getInstance(ImageLoaderConfig config) {
        if (mInstance == null) {
            synchronized (SimpleImageloader.class) {
                if (mInstance == null) {
                    mInstance = new SimpleImageloader(config);
                }
            }
        }
        return mInstance;
    }

    /**
     * 上一个是初始化时调用后，才能调用这一次的
     * 也就是说初始化完后，就可以调用无参数的了
     * 这个是第二次获取单例
     * @return
     */
    public static SimpleImageloader getInstance() {
        if (mInstance == null) {
           throw  new UnsupportedOperationException("没有初始化");
        }
        return mInstance;
    }

    /**
     * 调用显示方法
     * 这里uri ,调用层有可能是file: 有可能是http https
     */

    public void displayImage(ImageView imageView,String uri){
        displayImage(imageView,uri,null,null);
    }

    /**
     * 定制化
     *  中途设置显示的方式DisplayConfig, 和监听结果(调用层可能需要设置圆形图片啥的)
     */

    public void displayImage(ImageView imageView, String uri, DisplayConfig displayConfig,ImageListener imageListener){

        //实例化一个请求
        BitmapRequest bitmapRequest = new BitmapRequest(imageView,uri,displayConfig,imageListener);
        requestQueue.addBitmapRequest(bitmapRequest);

    }

    public static interface ImageListener{
        void onSuccess(ImageView imageView, Bitmap bitmap, String uri);
    }

    //获取全局配置，给请求队列使用
    public ImageLoaderConfig getImageoaderConfig() {
        return imageoaderConfig;
    }
}
