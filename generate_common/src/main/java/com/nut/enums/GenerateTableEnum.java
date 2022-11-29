package com.nut.enums;

/**
 * @author fym
 * @date 2022/11/28 21:59
 * @email 3271758240@qq.com
 */
public enum GenerateTableEnum {
    DATABASE_PASSWORD_OR_USER_ERROR(50001,"数据库用户名或密码错误"),
    DATABASE_NAME_NOT_EXIST(50002,"数据库名称不存在"),
    DATABASE_CONNECT(50003,"数据库连接失败"),
    SYSTEM_ERROR(50004,"系统错误"),
    DATABASE_PARAMETER_ERROR(50005,"数据库连接参数异常");
    private Integer code;

    private String message;


    GenerateTableEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }

}
