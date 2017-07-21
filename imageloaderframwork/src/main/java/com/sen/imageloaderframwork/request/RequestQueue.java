package com.sen.imageloaderframwork.request;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Administrator on 2017/7/18.
 */

public class RequestQueue {
    public static final String TAG = "sen";
    /**
     * 1. 使用阻塞式优先级别队列
     *  2.如果要使用PriorityBlockingQueue 那么泛型里BitmapRequest需要实现
     *  Comparator
     *  3.mRequestQueue 里的BitmapRequest 是否是同一个对象，需要实现hascode() equals()
     *
     *  特点：
     *  多线程共享，
     *  生产效率和消费效率相差太远
     *  每个产品都有一个编号
     *  优先级的优先消费，这里就根据BitmapRequest 中compareTo 决定优先级高低
     *
     */

    private BlockingQueue<BitmapRequest> mRequestQueue =  new PriorityBlockingQueue<>();

    //转发器数量
    private int threadCount ;
    //一组转发器
    private RequestDispatcher[] mRequestDispatcher;

    //线程安全的
    private AtomicInteger integer = new AtomicInteger(0);

    public RequestQueue(int defThreadCount) {
        this.threadCount = defThreadCount;
    }

//    提供添加

    public void addBitmapRequest(BitmapRequest bitmapRequest){
        if (!mRequestQueue.contains(bitmapRequest)){
            //给 Request 编号，不能直接i++ 或者++i 这种线程不安全的
            bitmapRequest.setSerialNum(integer.incrementAndGet());
            mRequestQueue.add(bitmapRequest);
        }else {
            Log.i(TAG,"请求已存在，编号为："+bitmapRequest.getSerialNum());
        }
    }

    /**
     * 开始转发器
     */
    public void start(){
        //跟开始动画一样，先停止，这样才安全
        stop();
        startRequestDispatcher();
    }
    //初始化并开始转发器
    private void startRequestDispatcher() {
        mRequestDispatcher = new RequestDispatcher[threadCount];
        for (int i= 0;i<threadCount;i++){
            RequestDispatcher dispatcher = new RequestDispatcher(mRequestQueue);
            mRequestDispatcher[i] = dispatcher;
            mRequestDispatcher[i].start();
        }
    }

    public void stop(){

    }


}
