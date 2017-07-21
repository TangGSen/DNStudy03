package com.sen.imageloaderframwork.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.sen.imageloaderframwork.request.BitmapRequest;

/**
 * Created by Administrator on 2017/7/20.
 */

public class DoubleCache implements BitmapCache{
    private MemoryCache memoryCache ;
    private DiskCache diskCache ;
    public DoubleCache(Context context){
       memoryCache = new MemoryCache();
        diskCache = DiskCache.getInstance(context);
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        memoryCache.put(request,bitmap);
        diskCache.put(request,bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        //先从内存取，在到硬盘取
        Bitmap bitmap = memoryCache.get(request);
        if (bitmap==null){
            bitmap = diskCache.get(request);
            if (bitmap!=null){
                memoryCache.put(request,bitmap);
            }
        }
        return bitmap;
    }

    @Override
    public void remove(BitmapRequest request) {
        memoryCache.remove(request);
        diskCache.remove(request);
    }
}
