package com.wxj.dao.impl;

import com.wxj.cache.CacheLoader;
import com.wxj.cache.annotation.Cacheable;
import com.wxj.dao.TestDao;
import com.wxj.entity.User;
import com.wxj.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangxinji
 */
@Repository
public class TestDaoImpl implements TestDao , CacheLoader {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserMapper userMapper;

    public String test() {
        List<Map<String,Object>> list = jdbcTemplate.queryForList("select * from user");
        return "success";
    }

    public List<User> findAll() {
        return userMapper.queryAll();
    }

    @Cacheable(cacheName = "userCache")
    public Map<String, Object> load() {
        List<User> users = userMapper.queryAll();
        Map<String,Object> map = new HashMap();
        for (User u:users) {
            map.put(u.getUserid(),u.getUsername());
        }
        return map;
    }
}
