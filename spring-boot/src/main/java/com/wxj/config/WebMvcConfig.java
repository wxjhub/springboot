package com.wxj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 扩展springmvc
 * @author wangxinji
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter{
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //重定向会暴漏真实的url地址，去请求controller
        //registry.addViewController("/").setViewName("redirect:/test/index");
        //转发不会暴漏真实的url地址，转发走的WEB-INF下直接找jsp
        registry.addViewController("/").setViewName("index");
        //registry.addViewController("/").setViewName("redirect:/test/sso");
    }

    /*@Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/*").setViewName("indexfree");
            }
        };
        return adapter;
    }*/
}
