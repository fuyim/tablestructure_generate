package com.nut.controller;

import com.nut.domain.dto.SqlTableParams;
import com.nut.servcice.GenService;
import com.nut.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author fym
 * @date 2023/3/15 18:21
 * @email 3271758240@qq.com
 */
@Controller
@RequestMapping("/gen")
@Slf4j
public class GenController {

    @Autowired
    private GenService genService;


    @RequestMapping(value =  "/genPreviewCode",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult genPreviewCode(@Valid @RequestBody SqlTableParams sqlTableParams){

        Map<String, String> dataMap = genService.genPreviewCode(sqlTableParams);

        return AjaxResult.success(dataMap);
    }

}
