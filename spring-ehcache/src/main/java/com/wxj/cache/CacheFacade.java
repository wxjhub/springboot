package com.wxj.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangxinji
 */
public final class CacheFacade {

    private volatile static CacheFacade instance = null;

    private CacheManager cacheManager;

    private CacheFacade() {

    }

    public static CacheFacade getCacheFacade() {
        if (instance == null) {
            synchronized (CacheFacade.class) {
                if (instance == null) {
                    instance = new CacheFacade();
                }
            }
        }
        return instance;
    }


    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 根据缓存名称和键获取缓存数据
     *
     * @param cacheName
     * @param key
     * @return
     */
    public Object getCacheData(String cacheName, String key) {
        Assert.hasText(cacheName);
        Assert.hasText(key);
        if (!cacheManager.cacheExists(cacheName)) {
            return null;
        }

        Cache cache = cacheManager.getCache(cacheName);
        if (!cache.isKeyInCache(key)) {
            return null;
        }
        return cache.get(key).getObjectValue();
    }

    /**
     * 获取缓存中的所有数据
     *
     * @param cacheName
     * @return
     */
    public Map<String, Object> getAllCacheData(String cacheName) {
        Assert.hasText(cacheName);
        if (!cacheManager.cacheExists(cacheName)) {
            return null;
        }
        Cache cache = cacheManager.getCache(cacheName);
        List<String> keys = cache.getKeys();
        Map<String, Object> result = new HashMap();
        for (String key : keys) {
            result.put(key, cache.get(key).getObjectValue());
        }
        return result;
    }

    /**
     * 缓存中存数据
     *
     * @param cacheName
     * @param key
     * @param o
     */
    public void setCacheData(String cacheName, String key, Object o) {
        Assert.hasText(cacheName);
        if (!cacheManager.cacheExists(cacheName)) {
            return;
        }
        Cache cache = cacheManager.getCache(cacheName);
        cache.put(new Element(key, o));
    }

    /**
     * 缓存中删除数据
     *
     * @param cacheName
     * @param key
     */
    public void removeCacheData(String cacheName, String key) {
        Assert.hasText(cacheName);
        if (!cacheManager.cacheExists(cacheName)) {
            return;
        }
        Cache cache = cacheManager.getCache(cacheName);
        cache.remove(key);
    }


    /**
     * 是否存在缓存数据
     *
     * @param cacheName
     * @return
     */
    public boolean cacheExists(String cacheName) {
        return cacheManager.cacheExists(cacheName);
    }

    /**
     * 获取缓存对象
     * @param cacheNam
     * @return
     */
    public Cache getCache(String cacheNam) {
        return cacheManager.getCache(cacheNam);
    }

}
