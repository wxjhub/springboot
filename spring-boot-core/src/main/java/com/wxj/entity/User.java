package com.wxj.entity;

import java.io.Serializable;

/**
 * @author wangxinji
 */
public class User implements Serializable {
    private static final long serialVersionUID = 8809101560720973267L;

    private String userid;

    private String username;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
