package com.nut.controller;

import com.nut.domain.DictData;
import com.nut.servcice.DictDataService;
import com.nut.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 后台全局字典处理
 * @author fym
 * @date 2023/1/17 16:28
 * @email 3271758240@qq.com
 */
@RequestMapping("/dict/data")
@Controller
@Slf4j
public class DictController {

    @Autowired
    private DictDataService dataService;


    @RequestMapping(value = "/type/{dictType}",method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult dictDataByType(@PathVariable String dictType){
        List<DictData> dictData = dataService.selectDictDataByType(dictType);
        return AjaxResult.success(dictData);
    }

}
