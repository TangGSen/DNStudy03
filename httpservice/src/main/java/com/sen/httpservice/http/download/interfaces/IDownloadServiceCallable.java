package com.sen.httpservice.http.download.interfaces;

import com.sen.httpservice.http.download.mode.DownloadInfo;

/**
 * Created by Administrator on 2017/7/13.
 * 这些回调都放生在主线程
 */

public interface IDownloadServiceCallable {

    void onDownloadStatusChanged(DownloadInfo downloadInfo);

    void onTotalLengthReceived(DownloadInfo downloadInfo);

    void onCurrentSizeChanged(DownloadInfo downloadInfo,double downLength,long seed);

    void onDownloadSucess(DownloadInfo downloadInfo);

    void onDownloadPause(DownloadInfo downloadInfo);

    void onDownloadError(DownloadInfo downloadInfo,int errorCode,String errorMsg);

}
