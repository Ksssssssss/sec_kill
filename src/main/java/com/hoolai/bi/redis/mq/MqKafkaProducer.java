package com.hoolai.bi.redis.mq;

import com.hoolai.bi.redis.config.KafkaProperties;
import kafka.Kafka;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-05-11 20:22
 */

@Component
public class MqKafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger("KafkaProducer");

    private final String topic = "test";
    private final KafkaProducer<Integer, String> producer;

    public MqKafkaProducer() {
        this.producer = new KafkaProducer(KafkaProperties.PROPERTIES);
    }

    public void send(String msg) {
        producer.send(new ProducerRecord<>(topic, msg));
    }
}
