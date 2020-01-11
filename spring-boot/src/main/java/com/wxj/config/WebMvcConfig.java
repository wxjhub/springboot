package com.wxj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 扩展springmvc
 * @author wangxinji
 */
/*@Configuration*/
public class WebMvcConfig extends WebMvcConfigurerAdapter{
    /*@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/*").setViewName("index");
    }*/

    @Bean
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/*").setViewName("indexfree");
            }
        };
        return adapter;
    }
}
