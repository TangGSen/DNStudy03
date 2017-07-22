package com.sen.xutilsframwork.annation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/7/22.
 * 这个类是对事件（点击事件，长按事件，item 点击事件），的拓展
 * <p>
 * 一个事件包括三方面:事件源，事件，回调
 * es:textview.setOnClickListern(new View.OnClickListenr(){
 * <p>
 * public void onClick()
 * })
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface EventBase {
    //设置监听的方法
    String listenerSetter();

    //事件类型
    Class<?> listenerType();

    //事件回调方法
    String callBackMethod();

}
