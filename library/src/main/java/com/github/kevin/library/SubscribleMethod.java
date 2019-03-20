package com.github.kevin.library;

import java.lang.reflect.Method;

public class SubscribleMethod {
    //回调方法
    private Method mMethod;
    //数据返回的线程模式
    private ThreadMode mThreadMode;
    //回调方法中的参数类
    private Class<?> type;

    public SubscribleMethod(Method method, ThreadMode threadMode, Class<?> type) {
        mMethod = method;
        mThreadMode = threadMode;
        this.type = type;
    }

    public Method getMethod() {
        return mMethod;
    }

    public void setMethod(Method method) {
        mMethod = method;
    }

    public ThreadMode getThreadMode() {
        return mThreadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        mThreadMode = threadMode;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
