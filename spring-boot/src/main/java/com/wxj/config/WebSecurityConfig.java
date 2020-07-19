package com.wxj.config;

import com.wxj.authsecurity.JdbcUserDetailsService;
import com.wxj.authsecurity.UseAuthenticationFailureHandler;
import com.wxj.authsecurity.UseAuthenticationSuccessHandler;
import com.wxj.filter.ProjectFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;;

/**
 * @author wangxj
 * 自定义springsecurity权限控制登录
 */
@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment environment;

    @Autowired
    private JdbcUserDetailsService userDetailsService;

    @Autowired
    private ProjectFilter projectFilter;

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login.jsp*");
    }

    public void configure(HttpSecurity http) throws Exception {
             http.csrf().disable()
                  .addFilterBefore(projectFilter, ChannelProcessingFilter.class)
                  //.addFilter(projectFilter)
                  .authorizeRequests().anyRequest().authenticated()
                  .and()
                  .authenticationProvider(getAuthenticationProvider())
                .formLogin()
                  .loginProcessingUrl("/login")
                  .loginPage("/login.jsp")
                  .permitAll()
                  .successHandler(getAuthenticationSuccessHandler())
                  .failureHandler(getAuthenticationFailureHandler())
                .and()
                  .logout()
                  .logoutUrl("/logout")
                  .logoutSuccessUrl("/index")
                  .deleteCookies("JSESSIONID")
                .and()
                  .sessionManagement()
                  .sessionFixation()
                  .migrateSession()
                  .maximumSessions(30)
                  //.sessionRegistry(sessionRegistry())
                  .maxSessionsPreventsLogin(true)
                .and()
                .and()
                  .exceptionHandling()
                  .accessDeniedPage("/403.jsp")
                  .and();

    }

    @Bean
    public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        UseAuthenticationSuccessHandler useAuthenticationSuccessHandler = new UseAuthenticationSuccessHandler();
        useAuthenticationSuccessHandler.setAlwaysUseDefaultTargetUrl(true);
        useAuthenticationSuccessHandler.setDefaultTargetUrl("/test/index");
        return useAuthenticationSuccessHandler;
    }

    @Bean
    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        UseAuthenticationFailureHandler useAuthenticationFailureHandler = new UseAuthenticationFailureHandler();
        useAuthenticationFailureHandler.setDefaultFailureUrl("/login.jsp?error");
        return useAuthenticationFailureHandler;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return  new StandardPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }
}
