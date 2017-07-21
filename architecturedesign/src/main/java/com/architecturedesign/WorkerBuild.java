package com.architecturedesign;

/**
 * 具体的建造者
 * 持有对房子的引用
 * Created by Administrator on 2017/7/6.
 */

public class WorkerBuild implements Build {
    Room room = new Room();
    @Override
    public void makeWindown() {
        room.setWindown("m窗户");
    }

    @Override
    public void makeFloor() {
        room.setFloor("m地板");
    }

    @Override
    public Room build() {
        return room;
    }
}
