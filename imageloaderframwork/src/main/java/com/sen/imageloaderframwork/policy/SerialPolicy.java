package com.sen.imageloaderframwork.policy;

import com.sen.imageloaderframwork.request.BitmapRequest;

/**
 * Created by Administrator on 2017/7/18.
 * 顺序的，先进先出
 */

public class SerialPolicy implements LoaderPolicy{
    @Override
    public int compareTo(BitmapRequest request1, BitmapRequest request2) {
        return request1.getSerialNum() -request2.getSerialNum();
    }
}
