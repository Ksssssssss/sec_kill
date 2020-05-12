package com.hoolai.bi.redis.redis.key;

public interface Key {
    int expireTime();

    String prefix();
}
