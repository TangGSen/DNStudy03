package com.sen.imageloaderframwork.request;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.sen.imageloaderframwork.config.DisplayConfig;
import com.sen.imageloaderframwork.loader.SimpleImageloader;
import com.sen.imageloaderframwork.policy.LoaderPolicy;
import com.sen.imageloaderframwork.utils.MD5Utils;

import java.lang.ref.SoftReference;

/**
 * Created by Administrator on 2017/7/18.
 * 请求队列也需要实现 Comparator,具体返回的值是加载策略的
 */

public class BitmapRequest implements Comparable<BitmapRequest>{
    //imageivew 比较大的对象，并且很可能被回收
    private SoftReference<ImageView> imageViewSoft ;

    private String imagUrl;
    //一些大公司的一些图片名字保存在sd卡中都是，字母串名字
    private String imageUriMD5;
    //下载完成的监听
    public SimpleImageloader.ImageListener imageListener;

    public DisplayConfig displayConfig;

    public BitmapRequest(ImageView imageView,String imagUrl, DisplayConfig displayConfig,SimpleImageloader.ImageListener imageListener) {
        this.imagUrl = imagUrl;
        this.imageListener = imageListener;
        this.imageViewSoft = new SoftReference<ImageView>(imageView);
        this.imageUriMD5 = MD5Utils.toMD5(imagUrl);
        //设置可见imageView 的tag
       // imageView.setTag(imagUrl);
        if (displayConfig !=null){
            this.displayConfig = displayConfig;
        }

    }

    //获取加载策略
    private LoaderPolicy loaderPolicy = SimpleImageloader.getInstance().getImageoaderConfig().getLoaderPolicy();

    //优先级编号
    private int serialNum;

    /**
     *  优先级的比较确定，交给加载策略决定
     */

    @Override
    public int compareTo(@NonNull BitmapRequest o) {
        return loaderPolicy.compareTo(this,o);
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BitmapRequest that = (BitmapRequest) o;

        if (serialNum != that.serialNum) return false;
        return loaderPolicy != null ? loaderPolicy.equals(that.loaderPolicy) : that.loaderPolicy == null;

    }

    @Override
    public int hashCode() {
        int result = loaderPolicy != null ? loaderPolicy.hashCode() : 0;
        result = 31 * result + serialNum;
        return result;
    }

    public ImageView getImageView(){
        return  imageViewSoft.get();
    }

    public String getImagUrl() {
        return imagUrl;
    }

    public String getImageUriMD5() {
        return imageUriMD5;
    }


}
