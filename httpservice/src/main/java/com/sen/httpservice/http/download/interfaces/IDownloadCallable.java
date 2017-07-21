package com.sen.httpservice.http.download.interfaces;

import com.sen.httpservice.http.download.enums.DownloadStatus;

/**
 * Created by Administrator on 2017/7/16.
 * 断点续传监听接口
 */

public interface IDownloadCallable {
    /**
     * 新增下载任务的监听
     *
     * @param downloadId
     */
    public void onDownlodInfoAdd(int downloadId);

    /**
     * 删除下载任务的监听
     *
     * @param downloadId
     */
    public void onDownlodInfoRemove(int downloadId);

    /**
     * 下载任务的状态变化监听
     *
     * @param downloadId
     */
    public void onDownloadStatusChanged(int downloadId, DownloadStatus downloadStatus);

    /**
     * 新增下载任务的监听
     *
     * @param downloadId
     */
    public void onTotalLengthReceived(int downloadId,int totalLength);

    /**
     * 下载进度
     *
     * @param downloadId
     */
    public void onCurrentSizeChanged(int downloadId,double present,long speed);

    /**
     * 下载成功
     *
     * @param downloadId
     */
    public void onDownloadSuccess(int downloadId);

    /**
     * 下载错误
     *
     * @param downloadId
     */
    public void onDownloadError(int downloadId,int errorCode,String errorMsg);


}
