package com.hoolai.bi.redis;

import com.hoolai.bi.redis.mq.MqKafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

@SpringBootTest
class KafkaTests {
    @Autowired
    private MqKafkaProducer producer;

    @Test
    void contextLoads() {
    }

    @Test
    public void generatorUserId() throws FileNotFoundException ,IOException{
        FileOutputStream outputStream = new FileOutputStream(new File("/Users/hoolai/Desktop/test-userid.txt"));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200000; i++) {
            sb.append(i);
            sb.append(",");
            sb.append("iphone10").append('\n');
        }
        outputStream.write(sb.toString().getBytes());
    }

    @Test
    public void randomNum(){

    }

    @Test
    public void kafka(){
        System.out.println(5/2);;
    }

}
