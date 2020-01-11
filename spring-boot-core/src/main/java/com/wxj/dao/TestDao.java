package com.wxj.dao;

import com.wxj.entity.User;

import java.util.List;

/**
 * @author wangxinji
 */
public interface TestDao {
    public String test();

    public List<User> findAll();
}
