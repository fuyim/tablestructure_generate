package com.nut.exception;

import com.nut.enums.GenerateTableEnum;
import com.nut.enums.SystemErrorEnum;

/**
 * @author fym
 * @date 2023/1/8 21:59
 * @email 3271758240@qq.com
 */
public class GlobalException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private final int code;

    public GlobalException(String message, int code) {
        super(message);
        this.code = code;
    }

    public GlobalException(SystemErrorEnum systemErrorEnum) {
        super(systemErrorEnum.getMessage());
        this.code = systemErrorEnum.getCode();
    }

    public GlobalException(SystemErrorEnum systemErrorEnum, String message){
        super(message);
        this.code = systemErrorEnum.getCode();
    }
}
