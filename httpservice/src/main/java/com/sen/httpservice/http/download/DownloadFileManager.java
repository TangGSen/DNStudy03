package com.sen.httpservice.http.download;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.sen.httpservice.http.HttpTask;
import com.sen.httpservice.http.RequestHodler;
import com.sen.httpservice.http.download.db.BaseDaoFactory;
import com.sen.httpservice.http.download.db.DownloadDao;
import com.sen.httpservice.http.download.enums.DownloadStatus;
import com.sen.httpservice.http.download.enums.DownloadStopMode;
import com.sen.httpservice.http.download.enums.Prority;
import com.sen.httpservice.http.download.interfaces.IDownloadCallable;
import com.sen.httpservice.http.download.interfaces.IDownloadServiceCallable;
import com.sen.httpservice.http.download.mode.DownloadInfo;
import com.sen.httpservice.http.interfaces.IHttpListener;
import com.sen.httpservice.http.interfaces.IHttpService;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017/7/13.
 */

public class DownloadFileManager implements IDownloadServiceCallable {
    private static final String TAG = "sen";
    DownloadDao downloadDao = BaseDaoFactory.getInstace().getDataHelper(DownloadDao.class, DownloadInfo.class);
    //赋予一个字节的锁
    private static byte[] lock = new byte[0];

    /**
     * 观察者模式
     */
    private List<IDownloadCallable> mAppListener = new CopyOnWriteArrayList<IDownloadCallable>();

    /**
     * 正在下载的任务集合
     */
    /**
     * 怎在下载的所有任务
     */
    private static List<DownloadInfo> downloadFileTaskList = new CopyOnWriteArrayList();

    private Handler mHandler = new Handler(Looper.getMainLooper());

//    public void readDownload(String url, String filePath) {
//        synchronized (lock) {
//            DownloadInfo downloadInfo = new DownloadInfo();
//            downloadInfo.setUrl(url);
//            downloadInfo.setFilePath(filePath);
//            RequestHodler requestHodler = new RequestHodler();
//
//            IHttpService httpService = new FileDownHttpService();
////            //得到请求头
////            Map<String, String> map = httpService.getHeaderMap();
//
//            IDownloadListener downloadListener = new DownloadListener(downloadInfo, this, httpService);
//
//            requestHodler.setHttpListener(downloadListener);
//            requestHodler.setHttpService(httpService);
//            requestHodler.setUrl(url);
//            try {
//                ThreadPoolManager.getInstace().execute(new HttpTask<>(requestHodler));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public int download(String url) {
        String[] priFix = url.split("/");
        return this.download(url, Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + priFix[priFix.length - 1]);
    }

    public int download(String url, String filePath) {
        String[] priFix = url.split("/");
        String disPlayName = priFix[priFix.length - 1];
        return this.download(url, filePath, disPlayName);
    }

    public int download(String url, String filePath, String disPlayName) {

        return this.download(url, filePath, disPlayName, Prority.middle);
    }

    public int download(String url, String filePath, String disPlayName, Prority prority) {
        if (prority == null) {
            prority = Prority.low;

        }

        File file = new File(filePath);
        //根据url 和路径来判断有没有下载历史
        DownloadInfo downloadInfo = downloadDao.findRecord(url, filePath);
        if (downloadInfo == null) {
            //如果没有下载,那么根据文件路径来查找
            List<DownloadInfo> downloadInfos = downloadDao.findRecord(filePath);
            //表示下载
            if (downloadInfos.size() > 0) {
                DownloadInfo info = downloadInfos.get(0);
                if (info.getCurrentLength() == info.getTotalLength()) {
                    synchronized (mAppListener) {
                        for (IDownloadCallable callable : mAppListener) {
                            callable.onDownloadError(info.getId(), 2, "文件已经下载完毕");
                        }
                    }
                }
            }
            //如果都没有下载,那么插入数据库
            downloadInfo = downloadDao.addRecord(url, filePath, disPlayName, prority.getValue());
            if (downloadInfo != null) {
                synchronized (mAppListener) {
                    for (IDownloadCallable callable : mAppListener) {
                        callable.onDownlodInfoAdd(downloadInfo.getId());
                    }
                }
            } //插入失败时，再次进行查找，确保能查得到
            else {
                //插入
                downloadInfo = downloadDao.findRecord(url, filePath);
            }
        }

        if (isDownloading(file.getAbsolutePath())) {
            synchronized (mAppListener) {
                for (IDownloadCallable callable : mAppListener) {
                    callable.onDownloadError(downloadInfo.getId(), 4, "正在下载，请不要重复添加");
                }
            }
            return downloadInfo.getId();
        }

        if (downloadInfo != null) {
            downloadInfo.setPriority(prority.getValue());
            //判断数据库存的，是否完成状态
            if (downloadInfo.getStatus() != DownloadStatus.finish.getValue()) {
                if (downloadInfo.getTotalLength() == 0L || file.length() == 0L) {
                    //还没开始
                    Log.e(TAG, "还没开始下载");

                    downloadInfo.setStatus(DownloadStatus.failed.getValue());
                }

                if (downloadInfo.getTotalLength() == file.length() && downloadInfo.getTotalLength() != 0) {
                    synchronized (mAppListener) {
                        for (IDownloadCallable callable : mAppListener) {
                            try {
                                //框架层在回调给调用层时使用try ,防止调用层报错，可用可不用
                                callable.onDownloadError(downloadInfo.getId(), 1, "已经下载完成");
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }
            //更新数据库
            downloadDao.updateRecord(downloadInfo);
        }

        //判断是否下载完成
        if (downloadInfo.getStatus() == DownloadStatus.finish.getValue()) {
            Log.e(TAG, "下载完成，回调给应用层");
            synchronized (mAppListener) {
                final int downloadId = downloadInfo.getId();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (IDownloadCallable callable : mAppListener) {
                            callable.onDownloadSuccess(downloadId);
                        }
                    }
                });
                downloadDao.removeRecordMenory(downloadId);
                return downloadId;
            }

        }

        //之前的下载，状态为暂停状态
        List<DownloadInfo> allDownloading = downloadFileTaskList;
        //当前下载不是最高级的，则先退出下载
        if (prority != Prority.high) {
            for (DownloadInfo downloading : allDownloading) {
                //从下载表中，获取到全部的正在下载的任务
                downloading = downloadDao.findSingleRecord(downloading.getFilePath());
                if (downloadInfo != null && downloadInfo.getPriority() == Prority.high.getValue()) {
                    if (downloadInfo.getFilePath().equals(downloading.getFilePath()))
                        return downloadInfo.getId();
                }

            }
        }

        DownloadInfo addDownloadInfo = readDownload(downloadInfo);
        if (prority == Prority.high || prority == Prority.middle) {
            synchronized (allDownloading) {
                for (DownloadInfo download : allDownloading) {
                    if (!downloadInfo.getFilePath().equals(download.getFilePath())) {
                        DownloadInfo downingInfo = downloadDao.findSingleRecord(download.getFilePath());
                        if (downingInfo != null) {
                            pause(downloadInfo.getId(), DownloadStopMode.auto);
                        }
                    }
                }
            }
            return downloadInfo.getId();
        }


        return -1;
    }


    /**
     * 下载
     */
    public DownloadInfo readDownload(DownloadInfo downloadInfo) {
        synchronized (lock) {
            //实例化DownloadItem
            RequestHodler requestHodler = new RequestHodler();
            //设置请求下载的策略
            IHttpService httpService = new FileDownHttpService();
            //得到请求头的参数 map
            Map<String, String> map = httpService.getHeaderMap();
            /**
             * 处理结果的策略
             */
            IHttpListener httpListener = new DownloadListener(downloadInfo, this, httpService);

            requestHodler.setHttpListener(httpListener);
            requestHodler.setHttpService(httpService);

            requestHodler.setUrl(downloadInfo.getUrl());

            HttpTask httpTask = new HttpTask(requestHodler);
            downloadInfo.setHttpTask(httpTask);

            /**
             * 添加
             */
            downloadFileTaskList.add(downloadInfo);
            httpTask.start();

        }

        return downloadInfo;

    }


    /**
     * 停止
     *
     * @param downloadId
     * @param mode
     */
    public void pause(int downloadId, DownloadStopMode mode) {
        if (mode == null) {
            mode = DownloadStopMode.auto;
        }
        final DownloadInfo downloadInfo = downloadDao.findRecordById(downloadId);
        if (downloadInfo != null) {
            // 更新停止状态
            if (downloadInfo != null) {
                downloadInfo.setStopMode(mode.getValue());
                downloadInfo.setStatus(DownloadStatus.pause.getValue());
                downloadDao.updateRecord(downloadInfo);
            }
            for (DownloadInfo downing : downloadFileTaskList) {
                if (downloadId == downing.getId()) {
                    downing.getHttpTask().pause();
                }
            }
        }
    }

    /**
     * 检查是否正在下载
     *
     * @param absolutePath
     * @return
     */
    private boolean isDownloading(String absolutePath) {
        for (DownloadInfo downloadInfo : downloadFileTaskList) {
            if (downloadInfo.getFilePath().equals(absolutePath))
                return true;
        }
        return false;
    }

    public void addListener(IDownloadCallable callable) {
        if (callable == null) {
            return;
        }

        synchronized (mAppListener) {
            if (mAppListener.contains(callable)) {
                return;
            }
            mAppListener.add(callable);
        }
    }

    @Override
    public void onDownloadStatusChanged(DownloadInfo downloadInfo) {

    }

    @Override
    public void onTotalLengthReceived(DownloadInfo downloadInfo) {

    }

    @Override
    public void onCurrentSizeChanged(DownloadInfo downloadInfo, double downLength, long seed) {

    }

    @Override
    public void onDownloadSucess(DownloadInfo downloadInfo) {

    }

    @Override
    public void onDownloadPause(DownloadInfo downloadInfo) {

    }

    @Override
    public void onDownloadError(DownloadInfo downloadInfo, int errorCode, String errorMsg) {

    }
}
