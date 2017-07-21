package com.sen.imageloaderframwork.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.sen.imageloaderframwork.request.BitmapRequest;

/**
 * Created by Administrator on 2017/7/18.
 */

public class MemoryCache implements BitmapCache {
    private LruCache<String ,Bitmap> mLruCache;

    public MemoryCache(){
        //设置缓存大小为可用内存的8分之一
        int menorySize = (int) (Runtime.getRuntime().freeMemory()/1024/8);
        mLruCache = new LruCache<String ,Bitmap>(menorySize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
            //  用一行的字节x高度
                return value.getRowBytes()*value.getHeight();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
//                oldValue.recycle();
//                oldValue = null;
               //
            }
        };
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        if (get(request)==null)
        mLruCache.put(request.getImageUriMD5(),bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest request) {

        return mLruCache.get(request.getImageUriMD5());
    }

    @Override
    public void remove(BitmapRequest request) {

        mLruCache.remove(request.getImageUriMD5());
    }
}
