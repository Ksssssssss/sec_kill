package com.hoolai.bi.redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Random;

@SpringBootTest
class RedisApplicationTests {

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
        Random random = new Random(47);
        System.out.println(random.nextInt(1000));
        System.out.println(random.nextInt(1000));
    }

    @Test
    public void cacheAvalanche(){

    }

}
