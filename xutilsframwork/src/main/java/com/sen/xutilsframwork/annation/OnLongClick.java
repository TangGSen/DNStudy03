package com.sen.xutilsframwork.annation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Administrator on 2017/7/22.
 * button.setOnLongClickListener(new View.OnLongClickListener() {
 *
 * @Override public boolean onLongClick(View v) {
 * return false;
 * }
 * });
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@EventBase(listenerSetter = "setOnLongClickListener",
        listenerType = View.OnLongClickListener.class,
        callBackMethod = "onLongClick")
public @interface OnLongClick {

    int [] value() default -1;
}
