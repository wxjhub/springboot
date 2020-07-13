package com.wxj.util;

import com.wxj.jdkproxy.SqlSessionProxy;

import java.lang.reflect.Proxy;

/**
 * JDK代理
 * @author wangxinji
 */
public class SqlSessionFactory {
    public static <T> T getMapper(Class<T> c) {
        return (T)Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c}, new SqlSessionProxy(c));
    }
}
