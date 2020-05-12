package com.hoolai.bi.redis.redis.key;

import com.hoolai.bi.redis.redis.key.Key;
import lombok.Data;
import org.apache.commons.lang.math.RandomUtils;

/**
 *
 *@description: 
 *@author: Ksssss(chenlin@hoolai.com)
 *@time: 2020-04-23 20:10
 * 
 */

@Data
public abstract class BaseKey implements Key {
    private final int expireTime;
    private final String prefix;

    public BaseKey(int expireTime, String prefix) {
        this.expireTime = expireTime;
        this.prefix = prefix;
    }

    public abstract Key buildDefaultKey();

    @Override
    public int expireTime() {
        return expireTime;
    }

    @Override
    public String prefix() {
        return prefix;
    }
}
