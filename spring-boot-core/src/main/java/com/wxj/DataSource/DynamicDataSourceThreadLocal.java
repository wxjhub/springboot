package com.wxj.DataSource;

//使用ThreadLocal存储为每个线程提供独立的数据源beanid副本,实现数据源切换
public class DynamicDataSourceThreadLocal {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    public static void puttl(String pj) {
        String s = threadLocal.get();
        if (s!=null && !"".equals(s)) {
            boolean contains = s.contains(",");
            if(contains) {
                int i = s.indexOf(",");
                String oldvalue = s.substring(0, i);
                String newvalue = oldvalue+","+pj;
                threadLocal.set(newvalue);
            }else {
                String newvalue = s+","+pj;
                threadLocal.set(newvalue);
            }
        }
        threadLocal.set(pj);
    }

    public static String gettl() {
        String s = threadLocal.get();
        if (s!=null && !"".equals(s)) {
            boolean contains = s.contains(",");
            if (contains) {
                int i = s.indexOf(",");
                String newvalue = s.substring(i+1);
                return newvalue;
            } else {
                return s;
            }
        } else {
            return s;
        }
    }

    public static void rmtl() {
        String s = threadLocal.get();
        if(s!=null && !"".equals(s)) {
            boolean contains = s.contains(",");
            if (contains) {
                int i = s.indexOf(",");
                String oldvalue = s.substring(0,i);
                threadLocal.set(oldvalue);
            }
        }
    }

    public static void clear() {
        threadLocal.remove();
    }
}
