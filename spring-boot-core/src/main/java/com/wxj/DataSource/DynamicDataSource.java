package com.wxj.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class DynamicDataSource implements DataSource, ApplicationContextAware {

    private ApplicationContext applicationContext = null;

    private DataSource dataSource=null;

    //每次连接数据库都要获取连接，根据选择的不同数据源名称，获取到对应的数据源，获取对应的数据库连接
    public Connection getConnection() throws SQLException {
        Connection connection = getDataSource().getConnection();
        return connection;
    }

    //项目启动将数据源注入进来
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //根据动态切换的beanid得到对应的数据源，实现切换，项目启动时候查询所有用户，将用户名作为beanid，数据源对象作为bean注册成bean
    public DataSource getDataSource() {
        String dsname = DynamicDataSourceThreadLocal.gettl();
        return getDataSource(dsname);
    }

    //从threadlocal中获取到的数据源名称为空，使用默认连接的数据库连接，否则从spring上下文中根据beanid获取到数据源bean
    public DataSource getDataSource(String dataSource) {
        if(dataSource==null || dataSource=="") {
            return this.dataSource;
        }
        DataSource ds = (DataSource) this.applicationContext.getBean(dataSource);
        return ds;
    }

    public Connection getConnection(String s, String s1) throws SQLException {
        return getDataSource().getConnection(s,s1);
    }

    public <T> T unwrap(Class<T> aClass) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> aClass) throws SQLException {
        return false;
    }

    public PrintWriter getLogWriter() throws SQLException {
        return getDataSource().getLogWriter();
    }

    public void setLogWriter(PrintWriter printWriter) throws SQLException {
        getDataSource().setLogWriter(printWriter);
    }

    public void setLoginTimeout(int i) throws SQLException {
        getDataSource().setLoginTimeout(i);
    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
