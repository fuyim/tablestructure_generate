package com.nut.enums;

/**
 * @author fym
 * @date 2023/1/8 22:01
 * @email 3271758240@qq.com
 */
public enum SystemErrorEnum {
    SUCCESS(200, "success"),
    PARAMS_ERROR(40000, "请求参数错误"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    ;


    private final int code;

    private final String message;

    SystemErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
