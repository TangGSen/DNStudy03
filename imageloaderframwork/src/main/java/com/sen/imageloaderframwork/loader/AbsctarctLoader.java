package com.sen.imageloaderframwork.loader;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.sen.imageloaderframwork.cache.BitmapCache;
import com.sen.imageloaderframwork.config.DisplayConfig;
import com.sen.imageloaderframwork.request.BitmapRequest;

/**
 * Created by Administrator on 2017/7/18.
 */

public abstract class AbsctarctLoader implements Loader {
    //拿到用户自定义的缓存策略
    BitmapCache bitmapCache = SimpleImageloader.getInstance().getImageoaderConfig().getBitmapCache();

    //拿到自定义的显示策略
    DisplayConfig displayConfig = SimpleImageloader.getInstance().getImageoaderConfig().getDisplayConfig();

    @Override
    public void loadImage(BitmapRequest bitmapRequest) {
        Bitmap bitmap = bitmapCache.get(bitmapRequest);
        if (bitmap == null) {
            //显示默认配置的加载图片
            showLoadingImage(bitmapRequest);
            bitmap = onLoad(bitmapRequest);
            cacheBitmap(bitmapRequest, bitmap);

        }

        deliveryToUIThread(bitmapRequest,bitmap);
//       if (bitmap!=null)
//           bitmap.recycle();

    }

    private void deliveryToUIThread(final BitmapRequest bitmapRequest, final Bitmap bitmap) {
        ImageView imageView = bitmapRequest.getImageView();
        if (imageView!=null){
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    updateImageView(bitmapRequest,bitmap);
                }
            });
        }
    }

    private void updateImageView(BitmapRequest bitmapRequest, Bitmap bitmap) {
        ImageView imageView = bitmapRequest.getImageView();
        //正常加载到bitmap
        if (bitmap !=null &&imageView.getTag().equals(bitmapRequest.getImagUrl())) {
            imageView.setImageBitmap(bitmap);
            Log.e("sen","bitmap !=null ");
        }
        //加载失败
        if (bitmap ==null && bitmapRequest.displayConfig!=null&&bitmapRequest.displayConfig.loadFaildImage>0) {
            imageView.setImageResource(bitmapRequest.displayConfig.loadFaildImage);
            Log.e("sen","bitmap ==null "+bitmapRequest.getImagUrl());
        }

        //监听回调，同时回传bitmap 可以其他处理，比如
        if (bitmapRequest.imageListener!=null)
            bitmapRequest.imageListener.onSuccess(imageView,bitmap,bitmapRequest.getImagUrl());
    }

    private void cacheBitmap(BitmapRequest bitmapRequest, Bitmap bitmap) {
        if (bitmapCache != null && bitmap != null && bitmapRequest != null) {
            synchronized (AbsctarctLoader.class) {
                bitmapCache.put(bitmapRequest, bitmap);
            }
        }
    }

    //抽象策略，加载网络和本地的策略给子类实现
    protected abstract Bitmap onLoad(BitmapRequest bitmapRequest);

    private void showLoadingImage(BitmapRequest bitmapRequest) {
        //就是用户配置了
        if (displayConfig != null && displayConfig.loaddingIamge > 0) {
            final ImageView imageView = bitmapRequest.getImageView();
            if (imageView != null) {
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(displayConfig.loaddingIamge);
                    }
                });
            }
        }
    }
}
