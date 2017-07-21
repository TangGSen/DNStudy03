package com.sen.httpservice.http.download;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sen.httpservice.http.download.interfaces.IDownloadListener;
import com.sen.httpservice.http.download.interfaces.IDownloadServiceCallable;
import com.sen.httpservice.http.download.mode.DownloadInfo;
import com.sen.httpservice.http.download.enums.DownloadStatus;
import com.sen.httpservice.http.interfaces.IHttpService;

import org.apache.http.HttpEntity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.sen.httpservice.http.download.enums.DownloadStatus.downloading;

/**
 * 这个类更多表现在，下载过程中状态的改变给调用层
 * Created by Administrator on 2017/7/14.
 */

public class DownloadListener implements IDownloadListener {
    public static final String TAG = "sen";
    private DownloadInfo downloadInfo;
    private File file;
    private String url;
    //已经下载的数量，也就是断点时下载好的长度
    private long breakPoint;
    private static final int DOWN_ERROR_CANCLE = 1;
    private static final int DOWN_ERROR_PAUSE = 2;

    private IDownloadServiceCallable downloadServiceCallable;

    private IHttpService httpService;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public DownloadListener(DownloadInfo downloadInfo, IDownloadServiceCallable downloadServiceCallable, IHttpService httpService) {

        this.downloadInfo = downloadInfo;
        this.downloadServiceCallable = downloadServiceCallable;
        this.httpService = httpService;
        this.file = new File(downloadInfo.getFilePath());
        this.breakPoint = file.length();


    }

    @Override
    public void onSuccess(HttpEntity httpEntity) {
        InputStream inputStream = null;
        try {
            inputStream = httpEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();
        //用于计算每秒多少k
        long speed = 0L;
        //花费的时间
        long useTime = 0L;
        //下载的长度
        long getLen = 0L;
        //接受的长度
        long receiveLen = 0L;
        boolean bufferLen = false;
        //得到的下载长度
        long dataLength = httpEntity.getContentLength();
        //单位时间下载的字节数
        long calcSpeedLen = 0L;
        //总数
        long totalLength = this.breakPoint + dataLength;
        //更新数量
        this.receviceTotalLength(totalLength);
        this.downloadStatusChange(downloading);
        byte[] buffer = new byte[512];//512最适合的
        int count = 0;
        long currentTime = System.currentTimeMillis();
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;

        if (!makeDir(this.getFile().getParentFile())) {
            downloadServiceCallable.onDownloadError(downloadInfo, 1, "创建文件夹失败");
        } else {
            try {
                fos = new FileOutputStream(this.getFile(), true);
                bos = new BufferedOutputStream(fos);
                int length = 1;
                while ((length = inputStream.read(buffer)) != -1) {
                    //首先判断是否停止
                    if (this.getHttpService().isCancle()) {
                        downloadServiceCallable.onDownloadError(downloadInfo, DOWN_ERROR_CANCLE, "用户取消");
                        return;
                    }
                    if (this.getHttpService().isPause()) {
                        downloadServiceCallable.onDownloadError(downloadInfo, DOWN_ERROR_PAUSE, "用户暂停");
                        return;
                    }

                    bos.write(buffer, 0, length);
                    getLen += length;
                    receiveLen += length;
                    calcSpeedLen += length;
                    ++count;
                    //receiveLen *10L /totalLength>=1L 每下载百分10回到一次，防止回调太多
                    if (receiveLen * 10L / totalLength >= 1L || count > 5000) {
                        currentTime = System.currentTimeMillis();
                        useTime = currentTime - startTime;
                        startTime = currentTime;
                        speed = 1000L * calcSpeedLen / useTime;
                        Log.e(TAG,"下载速度："+speed+"k/s");
                        count = 0;
                        calcSpeedLen = 0L;
                        receiveLen = 0L;
                        //在这里将数据保存在数据库里
                        this.downloadLengthChange(this.breakPoint + getLen, totalLength, speed);
                    }
                }
                bos.close();
                inputStream.close();
                if (dataLength != getLen) {
                    downloadServiceCallable.onDownloadError(downloadInfo, 3, "下载长度不相等");
                } else {
                    this.downloadLengthChange(this.breakPoint + getLen, totalLength, speed);
                    this.downloadServiceCallable.onDownloadSucess(downloadInfo.copy());
                    Log.e("sen","下载完成");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
//                if (this.getHttpService()!=null)
//                    this.getHttpService().abortRequest();
            } catch (IOException e) {
                e.printStackTrace();
//                if (this.getHttpService()!=null)
//                    this.getHttpService().abortRequest();

            } finally {

                try {
                    if (bos != null) {
                        bos.close();
                    }
                    if (httpEntity != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void downloadLengthChange(final long downLength, final long totalLength, final long speed) {
        /**
         *    写框架时，不能把DownloadInfo直接回调给调用成，必须要使用克隆模式，回调，防止调用成修改数据
         *    影响到底层框架
         */
        this.downloadInfo.setCurrentLength(downLength);
        if (downloadServiceCallable != null) {
            final DownloadInfo infoCopy = this.downloadInfo.copy();
            synchronized (this.downloadServiceCallable) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        downloadServiceCallable.onCurrentSizeChanged(infoCopy, downLength/totalLength, speed);
                    }
                });
            }

        }
    }

    /**
     * 更改下载时的状态
     *
     * @param downloading
     */
    private void downloadStatusChange(DownloadStatus downloading) {
        this.downloadInfo.setStatus(downloading.getValue());

        if (downloadServiceCallable != null) {
            final DownloadInfo infoCopy = this.downloadInfo.copy();
            synchronized (this.downloadServiceCallable) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        downloadServiceCallable.onDownloadStatusChanged(infoCopy);
                    }
                });
            }

        }

    }

    /**
     * 回到下载时长度的变化
     *
     * @param totalLength
     */
    private void receviceTotalLength(long totalLength) {
        this.downloadInfo.setTotalLength(totalLength);

        if (downloadServiceCallable != null) {
            final DownloadInfo infoCopy = this.downloadInfo.copy();
            synchronized (this.downloadServiceCallable) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        downloadServiceCallable.onTotalLengthReceived(infoCopy);
                    }
                });
            }

        }
    }

    private boolean makeDir(File parentFile) {
        return parentFile.exists() && (!parentFile.isFile()
                ? (parentFile.exists() && parentFile.isDirectory()) : parentFile.mkdir());
    }

    private File getFile() {
        return file;
    }

    public IHttpService getHttpService() {
        return httpService;
    }

    @Override
    public void onFail() {

    }

    @Override
    public void addHttpHead(Map<String, String> map) {
        long length = getFile().length();
        if (length>0L)
        map.put("RANGE","bytes="+length+"-");
    }

    @Override
    public void setHttpService(IHttpService httpService) {
        this.httpService = httpService;
    }



    @Override
    public void setCancleable() {

    }

    @Override
    public void setPauseable() {

    }
}
