package com.sen.imageloaderframwork.utils;

import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ImageViewHelper {
    //默认图片的宽高
    private static int DEF_WIDTH = 200;
    private static int DEF_HEIGTH = 200;

    /**
     * 1.getWidth 绘制完成，如果视图没有绘制完成没有值
     * 2.layout_width 有可能设置了WRAP_Content
     * 3.maxWidth 反射拿到
     * @param imageView
     * @return
     */
    public static int getImageViewWidth(ImageView imageView) {
        if (imageView != null) {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();

            int width = 0;
            if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT)
                width = imageView.getWidth();

            if (width <= 0 && params != null)
                width = params.width;
            //如果在小于0.那么通过反射获取，父控件给的最大值
            if (width <= 0)
                width = getImageViewFieldValue(imageView, "mMaxWidth");
            return width;
        }


        return DEF_WIDTH;
    }

    public static int getImageViewHeigth(ImageView imageView) {
        if (imageView != null) {
            ViewGroup.LayoutParams params = imageView.getLayoutParams();

            int height = 0;
            if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT)
                height = imageView.getHeight();

            if (height <= 0 && params != null)
                height = params.height;
            //如果在小于0.那么通过反射获取，父控件给的最大值
            if (height <= 0)
                height = getImageViewFieldValue(imageView, "mMaxHeight");
            return height;
        }


        return DEF_HEIGTH;
    }

    private static int getImageViewFieldValue(ImageView imageView, String fieldStirng) {
        try {
            Field field = ImageView.class.getDeclaredField(fieldStirng);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(imageView);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) ;
            return fieldValue;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
