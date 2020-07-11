package com.wxj.authsecurity;

import com.wxj.entity.User;
import com.wxj.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessageAware;
import java.util.ArrayList;
import java.util.List;

@Service
public class JdbcUserDetailsService implements UserDetailsService {

    @Autowired
    public PasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User u= new User();
        UserInfo userInfo = getUserInfo(u);
        return userInfo;
    }

    protected UserInfo getUserInfo(User user) {
        List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
        authorityList.add(new SimpleGrantedAuthority("1"));
        UserInfo userInfo = new UserInfo("wxj",passwordEncoder.encode("123"),authorityList);
        return userInfo;
    }

    public void setMessageSource(MessageSource messageSource) {

    }
}
