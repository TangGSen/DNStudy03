package com.sen.xutilsframwork.annation;

import android.content.Context;
import android.view.View;

import com.sen.xutilsframwork.proxy.ListenerInvoctionHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/21.
 */

public class InjectUtils {

    private Class<?> contextClass;

    public static void inject(Context context) {
        //注入视图
        injectLayout(context);
        //注入View
        injectView(context);

        //注入事件
        injectEvents(context);
    }


    private static void injectEvents(Context context) {
        //获取当前Activity，Fragment的
        Class<?> contextClass = context.getClass();
        //获取所有公开的方法方法
        Method[] methods = contextClass.getDeclaredMethods();
        for (Method method : methods) {
            //获取所有方法上的所有注解
            Annotation[] annotations = method.getAnnotations();
            for (Annotation anno : annotations) {
                //获取注解的类型，也就是对应的class
                Class<?> annType = anno.annotationType();
                //再获取注解上的注解，
                EventBase eventBase = annType.getAnnotation(EventBase.class);
                if (eventBase == null) {
                    continue;
                }
                //获取事件的三要素
                String listenerSetter = eventBase.listenerSetter();
                Class<?> listenerType = eventBase.listenerType();
                String callBackMethod = eventBase.callBackMethod();

                //方法名与方法Method 对应关系
                Map<String, Method> methodMap = new HashMap<>();
                methodMap.put(callBackMethod, method);

                try {
                    /**
                     * 这里通过反射来拿value
                     * annType 需要强转成
                     *onClick OnLongClick,才能value(),这里为了都能使用和拓展，不能使用具体的
                     * OnClick 或者OnLongClick获取value
                     */
                    Method valueMethod = annType.getDeclaredMethod("value");
                    int[] valueIds = (int[]) valueMethod.invoke(anno);

                    ListenerInvoctionHandler inHandler = new ListenerInvoctionHandler(context, methodMap);
                    Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(),
                            new Class[]{listenerType}, inHandler);
                    for (int valueId : valueIds) {
                        Method findViewById = contextClass.getMethod("findViewById", int.class);
                        View view = (View) findViewById.invoke(context, valueId);
                        if (view == null)
                            continue;
                        //反射获取view中的 setOnClickListener【方法名】(【参数】new View.OnClickLinstener);
                        Method setOnclickListern = view.getClass().getMethod(listenerSetter, listenerType);
                        /**
                         * 类比 于
                         * textView.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                        }
                        });
                         */
                        setOnclickListern.invoke(view,proxy);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private static void injectLayout(Context context) {
        //获取当前Activity，Fragment的
        Class<?> contextClass = context.getClass();
        //通过反射拿到Activity上的注解
        ContentView contentView = contextClass.getAnnotation(ContentView.class);
        int layoutId = contentView.value();
        try {
            Method method = contextClass.getMethod("setContentView", int.class);
            method.invoke(context, layoutId);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void injectView(Context context) {
        Class<?> contextClass = context.getClass();
        //得到当前所有的成员变量
        Field[] fields = contextClass.getFields();
        for (Field field : fields) {
            //判断有没有标记
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                try {
                    Method method = contextClass.getMethod("findViewById", int.class);
                    View view = (View) method.invoke(context, viewInject.value());
                    field.setAccessible(true);
                    field.set(context, view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
