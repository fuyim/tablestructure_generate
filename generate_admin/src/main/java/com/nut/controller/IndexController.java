package com.nut.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;

/**
 * @author fym
 * @date 2022/11/27 10:44
 * @email 3271758240@qq.com
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String goHome() throws SQLException {
        return "index";
    }

}
