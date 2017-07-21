package com.sen.imageloaderframwork.policy;

import com.sen.imageloaderframwork.request.BitmapRequest;

/**
 * Created by Administrator on 2017/7/18.
 * 倒序策略，后进先出
 */

public class ReversePolicy implements LoaderPolicy {
    @Override
    public int compareTo(BitmapRequest request1, BitmapRequest request2) {
        return request2.getSerialNum()-request1.getSerialNum();
    }
}
