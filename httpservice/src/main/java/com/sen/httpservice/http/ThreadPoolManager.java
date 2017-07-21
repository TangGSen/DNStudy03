package com.sen.httpservice.http;

import android.util.Log;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/**
 * Created by Administrator on 2017/7/12.
 * 做请求的生成者和消费者都要用到队列，
 */

public class ThreadPoolManager {
    private static String TAG = "sen";
    private static ThreadPoolManager mInstance;
    private ThreadPoolExecutor threadPoolExecutor ;
    //这里使用的是阻塞队列，在开发并发编程都需要
    private LinkedBlockingQueue<Future<?>> taskQueue = new LinkedBlockingQueue<>();
    private ThreadPoolManager(){
        threadPoolExecutor = new ThreadPoolExecutor(4,10,10, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(4),rejectedHandler);
        threadPoolExecutor.execute(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
           while (true){
               FutureTask task = null;
               try {
                   //taskqueue .take 阻塞式函数，如果有的话那么就执行下面代码，如果没就阻塞在这个函数
                   Log.e(TAG,"%%%%%当前等待队列："+taskQueue.size()+"线程池大小："+threadPoolExecutor.getPoolSize());
                   task = (FutureTask) taskQueue.take();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               if (task !=null){
                   threadPoolExecutor.execute(task);
               }
               Log.e(TAG,"*****当前等待队列："+taskQueue.size()+"线程池大小："+threadPoolExecutor.getPoolSize());

           }
        }
    };

    public static ThreadPoolManager getInstace(){
        if (mInstance==null){
            synchronized (ThreadPoolManager.class){
                if (mInstance ==null)
                    mInstance= new ThreadPoolManager();
            }
        }
        return mInstance;
    }

    /**
     * 拒绝策略，比如这里使用了10线程，你扔过来是11个或更多，这里就做了一个策略
     */
    private RejectedExecutionHandler rejectedHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                taskQueue.put(new FutureTask<Objects>(r,null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    };

    //给调用层调用的
    public <T> void execute(Runnable run) throws InterruptedException {
        taskQueue.put(new FutureTask<Object>(run,null));
    }

    public <T> boolean removeTask(FutureTask futureTask) {
        boolean result = false;
        /**
         * 阻塞式队列是否含有线程
         */
        if (taskQueue.contains(futureTask)) {
            taskQueue.remove(futureTask);
        } else {
            result = threadPoolExecutor.remove(futureTask);
        }
        return result;
    }

}
