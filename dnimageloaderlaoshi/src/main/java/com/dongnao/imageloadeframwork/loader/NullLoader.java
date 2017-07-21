package com.dongnao.imageloadeframwork.loader;

import android.graphics.Bitmap;

import com.dongnao.imageloadeframwork.request.BitmapRequest;

/**
 * Created by Administrator on 2017/2/8 0008.
 */

public class NullLoader extends  AbstarctLoader {
    @Override
    protected Bitmap onLoad(BitmapRequest request) {
        return null;
    }
}
