package com.architecturedesign;

/**
 * 抽象建造者
 * Created by Administrator on 2017/7/6.
 */

public interface Build {
    public void makeWindown();
    public void makeFloor();

    public Room build();
}
