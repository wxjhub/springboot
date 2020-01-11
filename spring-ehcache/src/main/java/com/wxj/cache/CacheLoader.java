package com.wxj.cache;

import java.util.Map;

/**
 *
 * @author wangxinji
 */
public interface CacheLoader {
    Map<String,Object> load();
}
