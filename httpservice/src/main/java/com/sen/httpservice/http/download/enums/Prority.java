package com.sen.httpservice.http.download.enums;

/**
 * Created by Administrator on 2017/7/16.
 */

public enum Prority {
    /**
     * 手动下载的优先级
     */
    low(0),
    /**
     * 主动推送资源，手动恢复的优先级
     */
    middle(1),
    /**
     * 主动推送资源的优先级
     */
    high(2);

    private  int value;

    Prority (int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public void setValue(int value){
        this.value = value;
    }

    public static Prority getIntance(int value){
        for (Prority prority :Prority.values()){
            if (prority.getValue() ==value){
                return prority;
            }
        }
        return Prority.middle;
    }
}
