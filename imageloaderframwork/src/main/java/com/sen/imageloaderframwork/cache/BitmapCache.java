package com.sen.imageloaderframwork.cache;

import android.graphics.Bitmap;

import com.sen.imageloaderframwork.request.BitmapRequest;

/**
 * Created by Administrator on 2017/7/18.
 */

public interface BitmapCache {
    /**
     * 添加缓存
     */
    void put(BitmapRequest request, Bitmap bitmap);

    /**
     * 通过请求取出bitmap
     * @param request
     * @return
     */
    Bitmap get(BitmapRequest request);

    /**
     * 通过请求移除缓存
     * @param request
     */
    void remove(BitmapRequest request);
}
