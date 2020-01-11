package com.wxj.mapper;

import com.wxj.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wangxinji
 */
@Repository
public interface UserMapper {
    List<User> queryAll();
}
