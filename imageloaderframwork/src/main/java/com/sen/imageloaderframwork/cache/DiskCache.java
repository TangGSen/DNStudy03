package com.sen.imageloaderframwork.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.sen.imageloaderframwork.disk.DiskLruCache;
import com.sen.imageloaderframwork.disk.IOUtil;
import com.sen.imageloaderframwork.request.BitmapRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/7/18.
 */

public class DiskCache implements BitmapCache {

    private static DiskCache mDiskCache;

    private   DiskCache(Context context){
        File dir = getCacheDir(mCacheDir,context);
        if (!dir.exists())
            dir.mkdir();

        try {
            diskLruCache =  DiskLruCache.open(dir,1,1,50L*MB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DiskCache getInstance(Context context){
        if (mDiskCache ==null){
            synchronized (DiskCache.class){
                if (mDiskCache ==null)
                    mDiskCache = new DiskCache(context);
            }
        }
        return mDiskCache;
    }

    private File getCacheDir(String mCacheDir, Context context) {
        //其实这里要使用Android/data/data/包名/cache/images/
        return new File(Environment.getExternalStorageDirectory(),mCacheDir);
    }

    private DiskLruCache diskLruCache;

    private String mCacheDir = "images";

    private final static long MB = 1024*1024;

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        if (diskLruCache!=null){
            DiskLruCache.Editor editor = null;
            OutputStream outputStream = null;
            try {
                editor= diskLruCache.edit(request.getImageUriMD5());
                outputStream = editor.newOutputStream(0);
                if(bitmap2Disk(bitmap,outputStream)){
                    editor.commit();
                }else{
                    //回退，将之前的都删除
                    editor.abort();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean bitmap2Disk(Bitmap bitmap, OutputStream outputStream) {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bufferedOutputStream);
        try {
            bufferedOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            IOUtil.closeQuietly(outputStream);
            IOUtil.closeQuietly(bufferedOutputStream);
        }
        return true;
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        InputStream inputStream = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(request.getImageUriMD5());
            //因为diskLruCache 能对同样的bitmap写入多次，内部会处理好，同时同一个key 会返回多个流，
            //这里取一个就行了
            if (snapshot!=null){
                //第一次肯定为空的
                inputStream  = snapshot.getInputStream(0);
                return BitmapFactory.decodeStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtil.closeQuietly(inputStream);
        }
        return null;
    }

    @Override
    public void remove(BitmapRequest request) {
        try {
            diskLruCache.remove(request.getImageUriMD5());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
