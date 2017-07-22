package com.sen.xutilsframwork.proxy;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/22.
 */

public class ListenerInvoctionHandler implements InvocationHandler {
    //代理的真实对象是Actiivity
    private Context context;
    private Map<String ,Method> map;

    public ListenerInvoctionHandler(Context context, Map<String, Method> map) {
        this.context = context;
        this.map = map;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        Method real = map.get(name);
        if (real!=null){
            return real.invoke(context,args);
        }else {
            //通过反射调用代理对象
            return method.invoke(proxy, args);
        }
    }
}
