package com.hoolai.bi.redis.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * @description:
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-04-19 17:01
 */

@Service
@Data
public class TimeService {
    private long currentTime;

    @PostConstruct
    private void init() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentTime = System.currentTimeMillis();
            }
        }).start();

    }

    public int findKthLargest(Integer[] nums, int k) {
        Arrays.sort(nums, (o1, o2) -> o1 - o2);
        return nums[k];
    }
}
