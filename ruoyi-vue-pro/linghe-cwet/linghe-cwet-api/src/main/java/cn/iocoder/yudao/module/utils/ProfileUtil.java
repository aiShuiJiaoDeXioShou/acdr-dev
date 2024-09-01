package cn.iocoder.yudao.module.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author linghe
 * 通过此工具类判断项目是否处于开发状态
 */
@Component
public class ProfileUtil {

    private static Environment environment;

    @Autowired
    public ProfileUtil(Environment environment) {
        ProfileUtil.environment = environment;
    }

    public static boolean isDebug() {
        String activeProfiles = environment.getProperty("spring.profiles.active");
        return "devp".equals(activeProfiles);
    }
}


