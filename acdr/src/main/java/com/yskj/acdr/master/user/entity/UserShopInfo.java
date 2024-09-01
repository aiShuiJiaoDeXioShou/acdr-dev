package com.yskj.acdr.master.user.entity;

import lombok.Data;

@Data
public class UserShopInfo {
    private int userId;
    private String accessToken;
    private String refreshToken;
    private long expiresTime;
    private String openid;
}
