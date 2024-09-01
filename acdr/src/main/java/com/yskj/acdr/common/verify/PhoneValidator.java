package com.yskj.acdr.common.verify;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null) {
            return false;
        }
        // 正则表达式可以根据需要调整
        return phoneField.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 隐藏手机号中间部分，只显示前三位和后三位
     * @param phoneNumber 完整的手机号
     * @return 隐藏后的手机号
     */
    public static String hidePhoneNumber(String phoneNumber) {
        if (StrUtil.isBlank(phoneNumber) || phoneNumber.length() != 11) {
            throw new IllegalArgumentException("手机号格式不正确");
        }
        // 使用 Hutool 的 StrUtil.subPre 和 StrUtil.subSuf 方法截取字符串
        String prefix = StrUtil.subPre(phoneNumber, 3); // 获取前三位
        String suffix = StrUtil.subSuf(phoneNumber, 4); // 获取后三位

        return prefix + "****" + suffix;
    }
}

