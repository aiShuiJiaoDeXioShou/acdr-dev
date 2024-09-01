package com.yskj.acdr.master.pet.status;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum PetExpertCertificationStatus {

    PENDING(0, "待审核"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已拒绝");

    @EnumValue
    private final int code;

    @JsonValue
    private final String description;

    PetExpertCertificationStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
