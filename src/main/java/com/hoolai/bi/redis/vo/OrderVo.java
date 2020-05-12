package com.hoolai.bi.redis.vo;

import lombok.Data;

/**
 * @description:
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-04-24 11:35
 */

@Data
public class OrderVo {
    private String orderId;
    private String userId;
    private String goodsId;

    public OrderVo() {
    }

    public OrderVo(String userId, String goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
        orderId = produceOrderId(userId,goodsId);
    }

    public static String produceOrderId(String userId, String goodsId) {
        return userId + "|" + goodsId;
    }
}
