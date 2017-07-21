package com.architecturedesign;

/**
 * 设计者，指导者
 * 他知道，房屋怎么建造，建造包工头所具有的能力有所了解，
 * 持有对包工头的引用
 * Created by Administrator on 2017/7/6.
 */

public class Designer {
    //传入包工头,指挥建造
    public Room build(Build build){
        build.makeFloor();
        build.makeWindown();
        return build.build();
    }

}
