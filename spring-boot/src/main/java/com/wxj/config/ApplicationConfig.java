package com.wxj.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wxj.DataSource.DynamicDataSource;
import com.wxj.cache.CacheFacade;
import net.sf.ehcache.CacheManager;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;

/**
 * @author wangxinji
 */
@Configuration
@ComponentScan(basePackages = {"com.wxj"})
@PropertySource({"classpath:/jdbc.properties"})
@EnableTransactionManagement
@MapperScan(basePackages = {"com.wxj.mapper"})
@EnableCaching
@EnableAspectJAutoProxy
public class ApplicationConfig {

    /**
     * 不知道为什么注入不了
     */
    @Autowired
    private CacheManager cacheManager;

    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.type}")
    private String type;

    //注入不同的数据库连接池
    @Bean(destroyMethod="")
    public DataSource dataSourceProvider() {
        if ("druid".equals(type)) {
            return dataSource();
        }
        return comboPooledDataSource();
    }

    //Druid数据库连接池
    private DataSource dataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setDriverClassName(driver);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }

    //cp30数据库连接池
    private DataSource comboPooledDataSource(){
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(driver);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        //连接池在回收数据库连接时是否自动提交事务。如果为false，则会回滚未提交的事务，如果为true，则会自动提交事务。default : false（不建议使用）
        dataSource.setAutoCommitOnClose(false);
        //连接池在无空闲连接可用时一次性创建的新数据库连接数,default : 3
        dataSource.setAcquireIncrement(5);
        //连接的最大空闲时间，如果超过这个时间，某个数据库连接还没有被使用，则会断开掉这个连接。如果为0，则永远不会断开连接,即回收此连接。default : 0 单位 s
        dataSource.setMaxIdleTime(600);
        //连接池中拥有的最大连接数，如果获得新连接时会使连接总数超过这个值则不会再获取新连接，而是等待其他连接释放，所以这个值有可能会设计地很大,default : 15
        dataSource.setMaxPoolSize(50);
        //连接池保持的最小连接数,default : 3
        dataSource.setMinPoolSize(3);
        //c3p0是异步操作的，缓慢的JDBC操作通过帮助进程完成。扩展这些操作可以有效的提升性能 通过多线程实现多个操作同时被执行。Default: 3
        dataSource.setNumHelperThreads(10);
        //设置连接未归还时间超时
        dataSource.setUnreturnedConnectionTimeout(20000);
        //打印checkout的连接时间超时，打印日志
        dataSource.setDebugUnreturnedConnectionStackTraces(true);
        return dataSource;
    }

    //jdbc使用数据库连接池进行数据库连接
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    //整合mybatis使用数据库连接池进行数据库连接
    @Bean
    public SqlSessionFactoryBean createSqlSessionFactoryBean() {
        SqlSessionFactoryBean sqlSessionFactoryBean =null;
        try {
            sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(getDataSource());
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            //能加载对各，使用通配符
            sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("classpath*:mapper/**/*.xml"));
            //sqlSessionFactoryBean.setConfigLocation("mabatis-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSessionFactoryBean;
    }

    /**
     * 定义解析jsp的视图解析器
     * @return
     */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setOrder(Ordered.LOWEST_PRECEDENCE);
        viewResolver.setContentType("text/html;charset=UTF-8");
        return viewResolver;
    }

    /**
     * 把自定义缓存注入进来
     * @return
     */
    @Bean
    public CacheFacade cacheFacade() {
        CacheFacade cacheFacade = CacheFacade.getCacheFacade();
        cacheFacade.setCacheManager(cacheManager);
        return  cacheFacade;
    }

    @Bean
    public DataSource getDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDataSource(dataSourceProvider());
        return dynamicDataSource;
    }

    /**
     * 如何不注入缓存工厂指定配置文件，会产生配置文件冲突
     * @return
     */
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("customizeEhcache.xml"));
        ehCacheManagerFactoryBean.setShared(true);
        return ehCacheManagerFactoryBean;
    }

    /**
     * 定义解析.ftl的视图解析器
     * @return
     */
    /*@Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setCache(true);
        freeMarkerViewResolver.setPrefix("");
        freeMarkerViewResolver.setPrefix(".ftl");
        freeMarkerViewResolver.setRequestContextAttribute("request");
        freeMarkerViewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        freeMarkerViewResolver.setContentType("text/html;charset=UTF-8");
        return  freeMarkerViewResolver;
    }*/

}
