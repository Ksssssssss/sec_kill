package com.hoolai.bi.redis.redis.key.impl;

import com.hoolai.bi.redis.redis.key.BaseKey;
import com.hoolai.bi.redis.redis.key.Key;

/**
 * @description:
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-04-23 20:13
 */

public class UserKey extends BaseKey {
    private static final int EXPIRE_TIME = 60 * 60 * 24;
    private static final String KEY_PREFIX = "user";

    public UserKey(int expireTime, String prefix) {
        super(expireTime, prefix);
    }

    @Override
    public Key buildDefaultKey() {
        return new UserKey(EXPIRE_TIME,KEY_PREFIX);
    }
}
