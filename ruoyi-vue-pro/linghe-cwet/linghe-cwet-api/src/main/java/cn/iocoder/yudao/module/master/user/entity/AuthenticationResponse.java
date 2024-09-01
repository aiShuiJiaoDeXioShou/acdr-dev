package cn.iocoder.yudao.module.master.user.entity;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String name;
    private String idNo;
    private String respMessage;
    private String respCode;
    private String province;
    private String city;
    private String county;
    private String birthday;
    private String sex;
    private String age;
}
