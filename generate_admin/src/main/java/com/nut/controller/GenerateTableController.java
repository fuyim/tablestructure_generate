package com.nut.controller;

import com.nut.domain.dto.DataBaseSourceDTO;
import com.nut.servcice.GenerateTableService;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@RequestMapping(value = "/generate")
public class GenerateTableController {


    @Autowired
    private GenerateTableService generateTableService;


    /**
     * 根据目标数据源生成excel
     */
    @RequestMapping(value = "/generateExcel",method = RequestMethod.POST)
    @ResponseBody
    public void generateExcel(@Valid @RequestBody DataBaseSourceDTO dto, BindingResult result, HttpServletResponse response) {

        generateTableService.generateExcel(dto,response);

    }

}
