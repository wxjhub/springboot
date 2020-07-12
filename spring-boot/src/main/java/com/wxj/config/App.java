package com.wxj.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author wangxinji
 */
/*@ComponentScan({"com.wxj.controller","com.wxj.service"})
@EnableAutoConfiguration*/
//自定义数据源，需要添加DataSourceAutoConfiguration否则冲突
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class App {
    //先用扫包的启动方式
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
