package com.nut.exception;

import com.nut.enums.GenerateTableEnum;

/**
 * @author fym
 * @date 2022/11/28 22:07
 * @email 3271758240@qq.com
 */
public class GenerateTableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code;

    public GenerateTableException(int code,String message) {
        super(message);
        this.code = code;
    }

    public GenerateTableException(GenerateTableEnum tableEnum){
        super(tableEnum.getMessage());
        this.code = tableEnum.getCode();
    }

    public GenerateTableException(GenerateTableEnum tableEnum,String message){
        super(message);
        this.code = tableEnum.getCode();
    }

    public int getCode() {
        return code;
    }
}
