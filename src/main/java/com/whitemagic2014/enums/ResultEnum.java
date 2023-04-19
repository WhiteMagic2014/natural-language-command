package com.whitemagic2014.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {

    SUCCESS(200, "成功"),
    ERROR(999, "错误"),
    ;


    private Integer code;

    private String desc;

}
