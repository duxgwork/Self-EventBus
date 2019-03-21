package com.github.kevin.library;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBus {
    private static volatile EventBus instance;
    //定义一个容器，保存所有的方法
    private Map<Object, List<SubscribleMethod>> cacheMap;
    private Handler mHandler;
    private ExecutorService mExecutorService;

    private EventBus() {
        cacheMap = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());//指定Handler在主线程环境
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    public static EventBus getDefault() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    public void register(Object obj) {
        List<SubscribleMethod> list = cacheMap.get(obj);
        if (list == null) {
            list = findSubscribleMethods(obj);
            cacheMap.put(obj, list);
        }

    }

    private List<SubscribleMethod> findSubscribleMethods(Object obj) {
        List<SubscribleMethod> list = new ArrayList<>();
        Class<?> clazz = obj.getClass();

        //循环查找父类是否存在Subscibe注解方法
        while (clazz != null) {
            //判断当前是否是系统类
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")) {
                break;
            }
            //得到所有的方法，这个clazz在本项目中指代的是MainActivity
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                //通过注解找到我们需要注册的方法
                Subscibe subscibe = method.getAnnotation(Subscibe.class);
                if (subscibe == null) {
                    continue;
                }
                //获取方法中的参数
                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1) {
                    throw new RuntimeException("EventBus只能接受一个参数");
                }
                //获取线程模式
                ThreadMode threadMode = subscibe.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method, threadMode, types[0]);
                list.add(subscribleMethod);
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    public void post(final Object type) {
        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            final Object obj = iterator.next();
            List<SubscribleMethod> list = cacheMap.get(obj);
            for (final SubscribleMethod subscribleMethod : list) {
                //简单的理解：两个类对比一下看是否一致（不严谨的说法）
                //a对象对应的类信息，是b对象的父类或父接口
                if (subscribleMethod.getType().isAssignableFrom(type.getClass())) {
                    switch (subscribleMethod.getThreadMode()) {
                        //主线程接收数据
                        case MAIN:
                            //主线程 ===> 主线程
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invoke(subscribleMethod, obj, type);
                            } else {//子线程 ===> 主线程
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, obj, type);
                                    }
                                });

                            }
                            break;
                        //子线程接收数据
                        case BACKGROUND:
                            //主线程 ===> 子线程
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                mExecutorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, obj, type);
                                    }
                                });
                            } else { //子线程 ===> 子线程
                                invoke(subscribleMethod, obj, type);
                            }
                            break;
                    }
                }
            }
        }
    }

    private void invoke(SubscribleMethod subscribleMethod, Object obj, Object type) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(obj, type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
