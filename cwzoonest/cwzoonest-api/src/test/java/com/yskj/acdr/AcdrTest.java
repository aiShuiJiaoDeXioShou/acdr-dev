package com.yskj.acdr;

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

}
