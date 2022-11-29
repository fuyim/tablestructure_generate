package com.nut.web.exception;

import com.nut.enums.GenerateTableEnum;
import com.nut.exception.GenerateTableException;
import com.nut.utils.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author fym
 * @date 2022/11/29 13:01
 * @email 3271758240@qq.com
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = GenerateTableException.class)
    public AjaxResult generateTableException(GenerateTableException e){
        log.error(e.getMessage(), e);
        return AjaxResult.error(e.getCode(),e.getMessage());
    }


    @ExceptionHandler(value = RuntimeException.class)
    public AjaxResult runtimeException(RuntimeException e){
        log.error(e.getMessage(),e);
        return AjaxResult.error(GenerateTableEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

}
