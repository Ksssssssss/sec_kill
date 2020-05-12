package com.hoolai.bi.redis.controller;

import com.hoolai.bi.redis.access.AccessLimit;
import com.hoolai.bi.redis.redis.RedisService;
import com.hoolai.bi.redis.redis.key.impl.GoodsKey;
import com.hoolai.bi.redis.redis.key.impl.OrderKey;
import com.hoolai.bi.redis.service.SecKillService;
import com.hoolai.bi.redis.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-04-24 11:04
 */
@RestController
@RequestMapping("secKill/")
public class GoodSeckillController {
    @Autowired
    private RedisService redisService;
    @Autowired
    private SecKillService seckillService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodSeckillController.class);
    private Map<String, Boolean> localCache = new HashMap<>();

    @PostMapping("/secKillGoods")
    @AccessLimit(actionId = "secKill",maxCount = "500",second = "1",capacity = "200")
    public void secKill(String userId, String goodsId) {

        //限制每个玩家点击次数
        boolean isLimit = redisService.simpleLimiting(userId,goodsId,1,5);
        if (!isLimit){
            LOGGER.info("玩家登陆频繁,玩家id{}",userId);
            return;
        }

        //判断玩家是否在订单列表里
        OrderVo orderVo = redisService.get(OrderKey.buildRandomExpireKey(), OrderVo.produceOrderId(userId, goodsId), OrderVo.class);
        if (orderVo != null) {
            LOGGER.info("玩家已经购买,无法购买，玩家id为{}", orderVo.getUserId());
            return;
        }

        //判断商品数量,本地缓存减少redis访问次数
        Boolean over = localCache.get(goodsId);
        if (over != null && over) {
            LOGGER.info("商品数量为0,无法购买");
            return;
        }

        int num = redisService.getNum(GoodsKey.KEY, goodsId);
        if (num < 1) {
            LOGGER.info("商品数量为{},无法购买", num);
            localCache.put(goodsId, true);
            return;
        }
        seckillService.secKillGoods(userId, goodsId);
    }

}
