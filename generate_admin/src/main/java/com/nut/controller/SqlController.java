package com.nut.controller;

import com.nut.annotation.RepeatSubmit;
import com.nut.domain.dto.SqlTableParams;
import com.nut.domain.vo.GenerateVO;
import com.nut.enums.SystemErrorEnum;
import com.nut.exception.GlobalException;
import com.nut.manager.facade.GenerateFacade;
import com.nut.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * 一 生成sql
 * @author fym
 * @date 2023/1/7 20:14
 * @email 3271758240@qq.com
 */
@Controller
@Slf4j
@RequestMapping("/sql")
public class SqlController {

    // TODO 生成sql语句
    @RepeatSubmit
    @RequestMapping(value = "/generateSql",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult generateSql(@Valid @RequestBody SqlTableParams sqlTableParams, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            throw new GlobalException(SystemErrorEnum.PARAMS_ERROR);
        }else {
            try {
                GenerateVO generateVO = GenerateFacade.generateCreateSql(sqlTableParams);
                return AjaxResult.success(generateVO);
            } catch (Exception e) {
                throw new GlobalException(SystemErrorEnum.SYSTEM_ERROR);
            }
        }
    }
}
