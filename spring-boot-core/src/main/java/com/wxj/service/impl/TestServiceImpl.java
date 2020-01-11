package com.wxj.service.impl;

import com.wxj.dao.TestDao;
import com.wxj.entity.User;
import com.wxj.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangxinji
 */

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestDao testDao;
    @Override
    public String test() {
        return testDao.test();
    }

    public List<User> findAll() {
        List<User> users = testDao.findAll();
        return users;
    }
}
