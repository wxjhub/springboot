package com.wxj.service.impl;

import com.wxj.dao.TestDao;
import com.wxj.entity.User;
import com.wxj.service.TestService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author wangxinji
 */

@Service
public class TestServiceImpl implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware,TestService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier(value = "dataSourceProvider")
    private DataSource dataSource;

    @Autowired
    private TestDao testDao;

    public String test() {
        return testDao.test();
    }

    public List<User> findAll() {
        List<User> users = testDao.findAll();
        return users;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext =applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent()==null) {
            ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
            //DataSourceProperties properties = new DataSourceProperties(dataSource.getClass());
            beanDefinitionBuilder.setParentName("dataSourceProvider");
            beanDefinitionBuilder.addPropertyValue("jdbcUrl","jdbc:mysql://192.168.142.128:3306/test");
            beanDefinitionBuilder.addPropertyValue("user","root");
            beanDefinitionBuilder.addPropertyValue("password","123456");
            beanFactory.registerBeanDefinition("10086", beanDefinitionBuilder.getRawBeanDefinition());
        }
    }
}
