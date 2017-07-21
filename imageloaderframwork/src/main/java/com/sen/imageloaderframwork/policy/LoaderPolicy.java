package com.sen.imageloaderframwork.policy;

import com.sen.imageloaderframwork.request.BitmapRequest;

/**
 * Created by Administrator on 2017/7/18.
 *加载策略
 */

public interface LoaderPolicy {

    int compareTo (BitmapRequest request1,BitmapRequest request2);
}
