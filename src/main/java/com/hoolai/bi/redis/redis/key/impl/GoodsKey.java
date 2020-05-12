package com.hoolai.bi.redis.redis.key.impl;

import com.hoolai.bi.redis.redis.key.BaseKey;
import com.hoolai.bi.redis.redis.key.Key;

/**
 * @description:
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-04-23 20:13
 */

public class GoodsKey extends BaseKey {
    private static final String KEY_PREFIX = "goods";
    private static final int EXPIRE_TIME = 0;
    public static final GoodsKey KEY = new GoodsKey();

    private GoodsKey() {
        super(EXPIRE_TIME, KEY_PREFIX);
    }

    @Override
    public Key buildDefaultKey() {
        return new GoodsKey();
    }
}
