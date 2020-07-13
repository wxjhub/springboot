package com.wxj.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author wangxinji
 */
public class SqlSessionProxy implements InvocationHandler {

    private Object target;

    public SqlSessionProxy(Object o) {
        this.target = o;
    }

    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return null;
    }
}
