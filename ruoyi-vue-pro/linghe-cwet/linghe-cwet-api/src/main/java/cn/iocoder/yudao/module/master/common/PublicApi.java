package cn.iocoder.yudao.module.master.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pet/public")
public class PublicApi {

    @GetMapping("/info")
    public String info() {
        return "GET INFO";
    }

}
