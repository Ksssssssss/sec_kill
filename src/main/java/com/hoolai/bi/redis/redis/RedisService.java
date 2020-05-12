package com.hoolai.bi.redis.redis;

import com.alibaba.fastjson.JSON;
import com.hoolai.bi.redis.redis.key.BaseKey;
import com.hoolai.bi.redis.redis.key.Key;
import com.hoolai.bi.redis.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @description:redis服务类
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-04-19 16:43
 */

@Component
public class RedisService {
    @Autowired
    private TimeService timeService;
    @Autowired
    private JedisProvider provider;

    private static final String DEFAULT_LIMIT_LUA = "return redis.call('cl.throttle',KEYS[1],ARGV[1],ARGV[2],ARGV[3],ARGV[4])[1]";

    /**
     * 简单限流
     *
     * @param udid
     * @param actionId
     * @param period
     * @param maxCount
     */
    public boolean simpleLimiting(String udid, String actionId, int period, int maxCount) {

        String key = udid + actionId;
        long now = timeService.getCurrentTime();
        Response<Long> currentCount = null;
        try (Jedis jedis = provider.provider();
             Pipeline pipeline = jedis.pipelined()) {
            pipeline.multi();
            pipeline.zadd(key, now, "" + now);
            pipeline.zremrangeByScore(key, 0, now - period * 1000);
            currentCount = pipeline.zcard(key);
            pipeline.expire(key, period + 1);
            pipeline.exec();
        }catch (Exception e){
            e.printStackTrace();
        }
        return currentCount.get() < maxCount;
    }

    /**
     * 漏斗限流
     *
     * @param actionId
     * @param capacity
     * @param second
     * @param maxCount
     * @param quota
     * @return
     */
    public boolean throttleLimit(String actionId, String capacity, String maxCount, String second, String quota) {
        long result = 0;
        try (Jedis jedis = provider.provider()) {
            result = (long)jedis.eval(DEFAULT_LIMIT_LUA, 1, actionId, capacity, maxCount, second, quota);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 0;
    }

    /**
     * @param baseKey
     * @param key
     * @param clazz
     * @param <T>
     * @return
     * @description getkey
     */
    public <T> T get(Key baseKey, String key, Class<T> clazz) {
        String realKey = baseKey.prefix() + key;
        T obj = null;
        try (Jedis jedis = provider.provider()) {
            String target = jedis.get(realKey);
            obj = parseObject(target, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     *
     */
    public int getNum(Key baseKey, String key) {
        String realKey = baseKey.prefix() + key;
        String target = "";
        try (Jedis jedis = provider.provider()) {
            target = jedis.get(realKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.isEmpty(target) ? 0 : Integer.parseInt(target);
    }

    public void decr(Key baseKey, String key) {
        String realKey = baseKey.prefix() + key;
        try (Jedis jedis = provider.provider()) {
            jedis.decr(realKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param baseKey
     * @param key
     * @param obj
     * @param <T>
     * @return
     * @description setkey并设置过期时间
     */
    public <T> boolean setex(Key baseKey, String key, T obj) {
        String realKey = baseKey.prefix() + key;
        int expireTime = baseKey.expireTime();
        String value = toString(obj);
        if (StringUtils.isEmpty(value)) {
            return false;
        }

        try (Jedis jedis = provider.provider()) {
            jedis.setex(realKey, expireTime, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 加载lua脚本
     *
     * @param lua
     * @return
     */
    public String loadLua(String lua) {
        String sha1 = "";
        try (Jedis jedis = provider.provider();) {
            sha1 = jedis.scriptLoad(lua);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sha1;
    }

    public Object scriptLua(BaseKey baseKey, String key, String lua) {
        String realKey = baseKey.getPrefix() + key;
        Object obj = null;
        try (Jedis jedis = provider.provider();) {
            obj = jedis.evalsha(lua, 1, realKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 执行lua脚本
     *
     * @param keys
     * @param params
     * @param lua
     * @return
     */
    public Object scriptLua(String lua, List<String> keys, List<String> params) {
        Object obj = null;
        try (Jedis jedis = provider.provider();) {
            obj = jedis.evalsha(lua, keys, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * obj转Stirng
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toString(T obj) {
        if (obj == null) {
            return null;
        }

        String result = "";
        try {
            result = JSON.toJSONString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * String转class
     *
     * @param target
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String target, Class<T> clazz) {
        if (StringUtils.isEmpty(target)) {
            return null;
        }
        return JSON.parseObject(target, clazz);
    }

    public static void main(String[] args) {
    }
}
