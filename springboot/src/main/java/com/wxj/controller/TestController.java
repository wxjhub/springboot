package com.wxj.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangxinji
 */
@Controller
public class TestController {

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "success";
    }
}
