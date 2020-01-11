package com.wxj.controller;

import com.wxj.cache.CacheFacade;
import com.wxj.entity.User;
import com.wxj.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @author wangxinji
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;


    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView m = new ModelAndView("index");
        String result = testService.test();
        return m;
    }

    @RequestMapping("/freemarker")
    public String freemarker(Map<String,Object> map) {
        map.put("name","王鑫吉");
        return "indexfree";
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public List<User> findAll() {
        List<User> users = testService.findAll();
        Map<String,Object> map = CacheFacade.getCacheFacade().getAllCacheData("userCache");
        return users;
    }

    public static void main(String[] args) {
        /*Map<String,Object> aa = new HashMap<>();
        aa.put("aa","aa");
        List<String> list2 = new ArrayList<>();
        List<String> list = new ArrayList(list1);*/
        List<String> list1 = new ArrayList();
        list1.add("11");
    }
}
