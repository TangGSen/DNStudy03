package com.sen.imageloaderframwork.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sen.imageloaderframwork.request.BitmapRequest;
import com.sen.imageloaderframwork.utils.BitmapDecoder;
import com.sen.imageloaderframwork.utils.ImageViewHelper;

import java.io.File;

/**
 * Created by Administrator on 2017/7/18.
 */

public class LoaclLoader extends AbsctarctLoader {
    @Override
    protected Bitmap onLoad(BitmapRequest bitmapRequest) {
        final String path = bitmapRequest.getImagUrl();
        File file = new File(path);
        if (!file.exists())
            return null;
        BitmapDecoder bitmapDecoder = new BitmapDecoder() {
            @Override
            public Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) {
                Bitmap bitmap = BitmapFactory.decodeFile(path,options);
                return bitmap;
            }
        };
        return bitmapDecoder.decodeBitmap(ImageViewHelper.getImageViewWidth(bitmapRequest.getImageView()),
                ImageViewHelper.getImageViewHeigth(bitmapRequest.getImageView()));
    }
}
