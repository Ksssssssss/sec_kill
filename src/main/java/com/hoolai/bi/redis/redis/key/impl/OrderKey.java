package com.hoolai.bi.redis.redis.key.impl;

import com.hoolai.bi.redis.redis.key.BaseKey;
import com.hoolai.bi.redis.redis.key.Key;
import org.apache.commons.lang.math.RandomUtils;

/**
 * @description:
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-04-24 11:36
 */

public class OrderKey extends BaseKey {
    private static final String DEFAULT_KEY_PREFIX = "order";
    private static final int DEFAULT_EXPIRE_TIME = 60 * 60 * 24 * 3;

    private OrderKey(int expireTime, String prefix) {
        super(expireTime, prefix);
    }

    @Override
    public Key buildDefaultKey() {
        return new OrderKey(DEFAULT_EXPIRE_TIME,DEFAULT_KEY_PREFIX);
    }

    public static OrderKey buildRandomExpireKey() {
        int randomExpireTime = DEFAULT_EXPIRE_TIME + RandomUtils.nextInt(DEFAULT_EXPIRE_TIME);
        return new OrderKey(randomExpireTime, DEFAULT_KEY_PREFIX);
    }

}
