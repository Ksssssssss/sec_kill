package com.hoolai.bi.redis.service;

import com.alibaba.fastjson.JSON;
import com.hoolai.bi.redis.mq.MqKafkaProducer;
import com.hoolai.bi.redis.redis.RedisService;
import com.hoolai.bi.redis.redis.key.impl.GoodsKey;
import com.hoolai.bi.redis.redis.key.impl.OrderKey;
import com.hoolai.bi.redis.vo.OrderVo;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-04-24 15:17
 */

@Service
@Data
public class SecKillService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private MqKafkaProducer producer;

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillService.class);

    private String decrLuaSha;
    private String produceOrderLuaSha;

    @PostConstruct
    private void init() {
        String decrLua = "if tonumber(redis.call('get', KEYS[1])) > tonumber('0')  then redis.call('decr',KEYS[1])  return 1 else return 0 end";
        String produceOrderLua = "if redis.call('get', KEYS[1]) == false then redis.call('setex',KEYS[1],ARGV[1],ARGV[2])  return 0 else return 1 end";
        decrLuaSha = redisService.loadLua(decrLua);
        produceOrderLuaSha = redisService.loadLua(produceOrderLua);
    }

    /**
     * @param userId
     * @param goodsId
     * @descption 生成订单，减少库存
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean secKillGoods(String userId, String goodsId) {
        Long stock = (Long) redisService.scriptLua(GoodsKey.KEY, goodsId, decrLuaSha);
        if (stock == null || stock == 0) {
            LOGGER.info("库存为{},无法购买", stock);
            return false;
        }

        OrderVo orderVo = new OrderVo(userId, goodsId);
        //判断是否重复购买
        List<String> params = Arrays.asList("" + OrderKey.buildRandomExpireKey().getExpireTime(), RedisService.toString(orderVo));
        String realKey = OrderKey.buildRandomExpireKey().getPrefix() + orderVo.getOrderId();
        Long isBuy = (Long) redisService.scriptLua(produceOrderLuaSha, Arrays.asList(realKey), params);

        if (isBuy == 1) {
            LOGGER.info("玩家{}已经购买,无法购买", userId);
            return false;
        }
        LOGGER.info("玩家{}购买成功", userId);
//        producer.send(JSON.toJSONString(orderVo));
        return true;
    }
}
