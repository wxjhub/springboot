package com.wxj.controller;

import com.wxj.authsecurity.JdbcUserDetailsService;
import com.wxj.cache.CacheFacade;
import com.wxj.entity.User;
import com.wxj.entity.UserInfo;
import com.wxj.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author wangxinji
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private JdbcUserDetailsService userDetailsService;


    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request,HttpSession httpSession) {
        ModelAndView m = new ModelAndView("index");
        String result = testService.test();
        httpSession.setAttribute("project","10086");
        return m;
    }

    @RequestMapping("/findschmer")
    @ResponseBody
    public ModelAndView findschmer(HttpServletRequest request,HttpSession httpSession) {
        ModelAndView m = new ModelAndView("test");
        List<User> result = testService.findAll();
        for (User user:result) {
            m.addObject("username",user.getUsername());
            break;
        }
        return m;
    }

    @RequestMapping("/sso")
    public String sso(HttpServletRequest request) {
        String username = "wxj";
        ssoAuth(username);
        //UserInfo principal = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpSession session = request.getSession();
        session.setAttribute("SPRING_SECURITY_CONTEXT",SecurityContextHolder.getContext());
        return "forward:index";
    }

    public void ssoAuth(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(userDetails);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
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
