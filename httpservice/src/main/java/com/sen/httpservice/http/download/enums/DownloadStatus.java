package com.sen.httpservice.http.download.enums;

/**
 * Created by Administrator on 2017/7/14.
 */

public enum DownloadStatus {
    waiting(0),

    start(1),

    downloading(2),

    finish(3),

    failed(4),

    pause(5);

    private  int value ;
    private DownloadStatus(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
