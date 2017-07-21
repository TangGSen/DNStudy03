package com.sen.imageloaderframwork.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.sen.imageloaderframwork.disk.IOUtil;
import com.sen.imageloaderframwork.request.BitmapRequest;
import com.sen.imageloaderframwork.utils.BitmapDecoder;
import com.sen.imageloaderframwork.utils.ImageViewHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/7/18.
 */

public class NetUrlLoader extends AbsctarctLoader {
    @Override
    protected Bitmap onLoad(final BitmapRequest bitmapRequest) {
        //先下载， 后读取
        downloadByUrl(bitmapRequest.getImagUrl(), getCache(bitmapRequest.getImageUriMD5()));
        BitmapDecoder bitmapDecoder = new BitmapDecoder() {
            @Override
            public Bitmap decodeBitmapWithOptions(BitmapFactory.Options options) {
                return BitmapFactory.decodeFile(getCache(bitmapRequest.getImageUriMD5()).getAbsolutePath(), options);
            }
        };
        /**
         * bitmapRequest.getImageView().getWidth() 这里不能使用这样方式
         * 有可能还没有绘制，获取的值为0；
         */
        return bitmapDecoder.decodeBitmap(ImageViewHelper.getImageViewWidth(bitmapRequest.getImageView()),
                ImageViewHelper.getImageViewHeigth(bitmapRequest.getImageView()));

    }

    public boolean downloadByUrl(String urlString, File file) {
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            inputStream = connection.getInputStream();
            fos = new FileOutputStream(file);
            byte[] buf = new byte[512];
            int len = 0;
            while ((len = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.closeQuietly(inputStream);
            IOUtil.closeQuietly(fos);
        }
        return false;
    }

    private File getCache(String unique) {
        File file = new File(Environment.getExternalStorageDirectory(), "ImageLoader");
        if (!file.exists())
            file.mkdir();
        return new File(file, unique);
    }
}
