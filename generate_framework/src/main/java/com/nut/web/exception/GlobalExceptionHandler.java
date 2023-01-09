package com.nut.web.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.nut.enums.GenerateTableEnum;
import com.nut.enums.SystemErrorEnum;
import com.nut.exception.GenerateTableException;
import com.nut.exception.GlobalException;
import com.nut.manager.facade.GenerateFacade;
import com.nut.utils.AjaxResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author fym
 * @date 2022/11/29 13:01
 * @email 3271758240@qq.com
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get(GlobalExceptionHandler.class);


    @ExceptionHandler(value = GenerateTableException.class)
    public AjaxResult generateTableException(GenerateTableException e){
        log.error(e.getMessage(), e);
        return AjaxResult.error(e.getCode(),e.getMessage());
    }

    public AjaxResult globalException(GlobalException e){
        log.error(e.getMessage(),e);
        return AjaxResult.error(e.getMessage());
    }


    @ExceptionHandler(value = RuntimeException.class)
    public AjaxResult runtimeException(RuntimeException e){
        log.error(e.getMessage(),e);
        return AjaxResult.error(GenerateTableEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

}
