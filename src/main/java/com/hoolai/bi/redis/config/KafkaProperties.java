package com.hoolai.bi.redis.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaProperties {
    public static final Properties PROPERTIES = new Properties();

    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("kafka.properties");
        try {
            PROPERTIES.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}