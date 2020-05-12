package com.hoolai.bi.redis.vo;

import lombok.Data;

/**
 * @description:
 * @author: Ksssss(chenlin @ hoolai.com)
 * @time: 2020-04-24 11:19
 */

@Data
public class UserVo {
    private String userId;
    private String name;
    private String pass;

    public UserVo() {
    }
}
