package com.sen.httpservice.http.download.mode;

import com.sen.httpservice.http.HttpTask;
import com.sen.httpservice.http.download.db.annotation.DbTable;

/**
 * Created by Administrator on 2017/7/13.
 */
@DbTable("t_downloadInfo")
public class DownloadInfo extends BaseEntity<DownloadInfo>{
    public long currentLength;

    public long totalLength;

    public transient HttpTask httpTask;
    /**
     * 下载id
     */
    public Integer id;

    /**
     * 下载url
     */
    public String url;

    /**
     * 下载存储的文件路径
     */
    public String filePath;

    /**
     * 下载文件显示名
     */
    public String displayName;

    /**
     * 下载开始时间
     */
    public String startTime;

    /**
     * 下载结束时间
     */
    public String finishTime;

    /**
     * 用户id
     */
    public String userId;

    /**
     * 下载任务类型
     */
    public String httpTaskType;

    /**
     * 下载优先级
     */
    public Integer priority;

    /**
     * 下载停止模式
     */
    public Integer stopMode;


    //下载的状态
    public Integer status;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHttpTaskType() {
        return httpTaskType;
    }

    public void setHttpTaskType(String httpTaskType) {
        this.httpTaskType = httpTaskType;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStopMode() {
        return stopMode;
    }

    public void setStopMode(Integer stopMode) {
        this.stopMode = stopMode;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public HttpTask getHttpTask() {
        return httpTask;
    }

    public void setHttpTask(HttpTask httpTask) {
        this.httpTask = httpTask;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
