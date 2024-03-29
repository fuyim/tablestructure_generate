package com.nut.controller;

import com.nut.annotation.RepeatSubmit;
import com.nut.domain.dto.DataBaseSourceDTO;
import com.nut.enums.GenerateTableEnum;
import com.nut.exception.GenerateTableException;
import com.nut.servcice.GenerateTableService;
import com.nut.utils.AjaxResult;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @author fym
 * @date 2022/11/23 12:35
 * @email 3271758240@qq.com
 */
@Controller
@Slf4j
@RequestMapping(value = "/generate")
public class GenerateTableController {


    @Autowired
    private GenerateTableService generateTableService;


    /**
     * 根据目标数据源生成excel
     * FIXME
     */
    @RequestMapping(value = "/generateExcel",method = RequestMethod.POST)
    @ResponseBody
    public void generateExcel(@Valid DataBaseSourceDTO dto, BindingResult result, HttpServletResponse response) {

        boolean hasErrors = result.hasErrors();
        if (hasErrors){
            throw new GenerateTableException(GenerateTableEnum.DATABASE_PARAMETER_ERROR);
        }else {
            try {
                generateTableService.generateExcel(dto,response);
            } catch (RuntimeException e) {
                throw new GenerateTableException(GenerateTableEnum.SYSTEM_ERROR,"连接异常,检查连接");
            }
        }
    }

    // FIXME 代码需要更改
    @RequestMapping(value = "/generateWord",method = RequestMethod.POST)
    @ResponseBody
    public void generateWord(@Valid DataBaseSourceDTO dto,BindingResult result,HttpServletResponse response){
        boolean hasErrors = result.hasErrors();
        if (hasErrors){
            throw new GenerateTableException(GenerateTableEnum.DATABASE_PARAMETER_ERROR);
        }else {
            try {
                generateTableService.generateWord(dto,response);
            } catch (RuntimeException e) {
                throw new GenerateTableException(GenerateTableEnum.SYSTEM_ERROR,"连接异常,检查连接");
            }
        }
    }

    /**
     * TODO 解析出表字段返回给前端生成表格，可以通过复制键将表格复制成文本
     * TODO 通过文本生成excel文档或者word文档，也可在sql生成页导入文本生成sql脚本
     */


}
