package com.yskj.acdr;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yskj.acdr.master.user.entity.AuthenticationResponse;
import com.yskj.acdr.master.user.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Slf4j
public class AcdrTest {

    @Autowired
    private AuthenticationService service;

    // 测试实名认证
    @Test
    void TestAuthenticationService() {
        AuthenticationResponse authenticate = service.authenticate("杨腾", "431121200209054718");
        System.out.println(authenticate);
    }

    // 测试身份证信息识别
    @Test
    void TestRecognizeIDCard() {
        Map<String, Object> face = service.recognizeIDCard("D:\\App\\Work\\addr\\acdr\\src\\test\\resources\\images\\2.jpg", "face");
        log.info(face.toString());
    }

    // 生成验证图片接口
    @Test
    void TestValidImage() {

    }

    // 商城端登录功能
    @Test
    void TestShop() {
        // 设置请求头，包括租户信息
        Map<String, String> headers = Map.of(
                "tenant-id", "1", // 这里设置租户ID为1
                "Content-Type", "application/json;charset=UTF-8"
        );

        // 设置请求体参数
        String requestBody = JSONUtil.toJsonStr(Map.of(
                "mobile", "19151950915",
                "password", "acdr0915"
        ));

        // 发送带有租户信息的POST请求
        String post = HttpUtil.createPost("http://127.0.0.1:48080/app-api/member/auth/registered")
                .addHeaders(headers) // 添加请求头
                .body(requestBody) // 添加请求体
                .execute() // 执行请求
                .body(); // 获取响应体

        log.info(post);
    }
}
