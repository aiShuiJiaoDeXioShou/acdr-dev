package com.yskj.acdr.master.order.orderenum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum PayType {

    EXPENSE(1, "支出"),
    INCOME(2, "收入");

    @EnumValue
    private final int code;  // 用于数据库中的整数值
    private final String description;  // 描述信息

    PayType(int code, String description) {
        this.code = code;
        this.description = description;
    }

}

