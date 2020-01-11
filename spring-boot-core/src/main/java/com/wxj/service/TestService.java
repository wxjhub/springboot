package com.wxj.service;

import com.wxj.entity.User;

import java.util.List;

/**
 * @author wangxinji
 */
public interface TestService {
    public String test();

    public List<User> findAll();
}
