package com.sen.imageloaderframwork.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2017/7/19.
 */

public abstract class BitmapDecoder {
    /**
     * reqWidth 是控件框宽高
     * @param reqWidth
     * @param reqHeigth
     */
    public Bitmap decodeBitmap(int reqWidth, int reqHeigth){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //根据option 加载bitmap对象
        decodeBitmapWithOptions(options);
        //计算图片图片的缩放比例
        calculateSampleSizeWithOptions(options,reqWidth,reqHeigth);

        return  decodeBitmapWithOptions(options);
    }

    private void calculateSampleSizeWithOptions(BitmapFactory.Options options, int reqWidth, int reqHeigth) {
        //图片的原始宽高
        int bitWith = options.outWidth;
        int bitHeight = options.outHeight;

        int sampleSize = 1;

        if (bitWith>reqWidth || bitHeight>reqHeigth){
            //宽高的缩放比例
            int widthRatio = Math.round(bitWith/reqWidth);
            int heightRatio = Math.round(bitHeight/reqHeigth);
            //有的图宽长，或高长
            sampleSize = Math.max(widthRatio,heightRatio);
        }

        options.inSampleSize = sampleSize;
        options.inPreferredConfig= Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;// 允许可清除
        options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果

    }

    public abstract  Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) ;
}
