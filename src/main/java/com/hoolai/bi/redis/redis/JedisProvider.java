package com.hoolai.bi.redis.redis;

import com.hoolai.bi.redis.redis.config.RedisConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

/**
 * @description:
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-03-30 10:16
 */

@Component
public class JedisProvider {

    @Autowired
    private RedisConfig redisConfig;

    private JedisPool jedisPool;

    @PostConstruct
    public void afterPropertiesSet() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(redisConfig.getMaxIdle());
        config.setMaxTotal(redisConfig.getMaxTotal());
        config.setMinIdle(redisConfig.getMinIdle());
        config.setTestOnBorrow(redisConfig.isTestOnBorrow());
        config.setTestOnReturn(redisConfig.isTestOnReturn());

        jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort());
    }

    public Jedis provider() {
        return jedisPool.getResource();
    }
}
