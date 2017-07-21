package com.sen.imageloaderframwork.loader;

import android.graphics.Bitmap;

import com.sen.imageloaderframwork.request.BitmapRequest;

/**
 * Created by Administrator on 2017/7/19.
 */

public class NullLoader extends AbsctarctLoader {
    @Override
    protected Bitmap onLoad(BitmapRequest bitmapRequest) {
        return null;
    }
}
