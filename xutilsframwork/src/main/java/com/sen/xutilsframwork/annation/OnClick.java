package com.sen.xutilsframwork.annation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/7/22.
 * <p>
 * button.setOnClickListener(new View.OnClickListener() {
 *
 * @Override public void onClick(View v) {
 * <p>
 * }
 * });
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@EventBase(listenerSetter = "setOnClickListener",
        listenerType = View.OnClickListener.class,
        callBackMethod = "onClick")
public @interface OnClick {
    //给那些id进行设置
    int[] value() default -1;
}
