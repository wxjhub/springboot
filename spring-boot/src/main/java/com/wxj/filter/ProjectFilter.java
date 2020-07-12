package com.wxj.filter;

import com.alibaba.druid.util.StringUtils;
import com.sun.net.httpserver.Filter;
import com.wxj.DataSource.DynamicDataSource;
import com.wxj.DataSource.DynamicDataSourceThreadLocal;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
public class ProjectFilter extends GenericFilterBean {

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String project = (String) httpServletRequest.getSession().getAttribute("project");
        if(project!=null && !"".equals(project)) {
            DynamicDataSourceThreadLocal.puttl(project);
        } else {
            DynamicDataSourceThreadLocal.puttl("");
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
