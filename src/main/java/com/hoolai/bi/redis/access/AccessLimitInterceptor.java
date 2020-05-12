package com.hoolai.bi.redis.access;

import com.hoolai.bi.redis.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *@description: 
 *@author: Ksssss(chenlin@hoolai.com)
 *@time: 2020-05-11 11:26
 * 
 */

@Service
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLimitInterceptor.class);
    @Autowired
    private RedisService service;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean flag = false;
        if (handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null){
                return true;
            }
            if (!(flag = accessLimit(accessLimit))){
                LOGGER.info("请稍等");
            }
        }
        return flag;
    }

    private boolean accessLimit(AccessLimit accessLimit) {
        String actionId = accessLimit.actionId();
        String second = accessLimit.second();
        String maxCount = accessLimit.maxCount();
        String capacity = accessLimit.capacity();
        String quota = accessLimit.quota();
        return service.throttleLimit(actionId,capacity,maxCount,second,quota);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
