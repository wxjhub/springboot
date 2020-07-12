package com.wxj.cache;

import com.wxj.cache.annotation.Cacheable;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.util.Map;

/**
 * @author wangxinji
 */
@Aspect
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public final class EhCacheInterceptor implements ApplicationContextAware , InitializingBean {

    @Autowired
    private CacheFacade cacheFacade;

    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
          this.applicationContext = applicationContext;
    }

    @AfterReturning(pointcut = "execution(* com.wxj..*.load()) && @annotation(cacheable)",
    returning = "returnValue")
    public Object cacheLoadInterceptor(final Cacheable cacheable, Object returnValue) {
        if (!cacheFacade.cacheExists(cacheable.cacheName())) {
            return returnValue;
        }

        Cache cache = cacheFacade.getCache(cacheable.cacheName());
        cache.removeAll();

        Map<String,Object> map = (Map<String, Object>) returnValue;
        putCacheValues(cache,map);
        return returnValue;
    }

    @Around("execution(* com.wxj..*.*(*,..)) && @annotation(cacheable)")
    public Object cacheableInterceptor(final ProceedingJoinPoint joinPoint, final Cacheable cacheable) throws Throwable {
        if (!cacheFacade.cacheExists(cacheable.cacheName())) {
            return joinPoint.proceed();
        }
        Cache cache = cacheFacade.getCache(cacheable.cacheName());
        Object[] parameterValues = joinPoint.getArgs();
        String[] parameterNames = getParameteNames((MethodSignature)joinPoint.getSignature());
        if(parameterNames==null) {
            return joinPoint.proceed();
        }
        String key = getkeyValue(cacheable.key(),parameterValues,parameterNames);
        if (cache.isKeyInCache(key)) {
            return cache.get(key).getObjectValue();
        }

        Object returnValue = joinPoint.proceed();
        cache.put(new Element(key,returnValue));
        Map<String,Object> map = (Map<String, Object>) returnValue;
        putCacheValues(cache,map);
        return returnValue;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(applicationContext);
        Assert.notNull(cacheFacade);
        Map<String, CacheLoader> beans = applicationContext.getBeansOfType(CacheLoader.class);
        for(CacheLoader loader:beans.values()) {
            loader.load();
        }
    }

    //緩存
    private void putCacheValues(Cache cache,Map<String,Object> cacheValue) {
        Assert.notNull(cache);
        cache.removeAll();
        if (cacheValue == null || cacheValue.isEmpty()) {
            return;
        }
        for (String key: cacheValue.keySet()) {
            cache.put(new Element(key,cacheValue.get(key)));
        }
    }

    private String[] getParameteNames(MethodSignature methodSignature) {
        return null;
    }

    private String getkeyValue(String key,Object[] parameterValues,String... parameterNames) {
        return "";
    }
}
