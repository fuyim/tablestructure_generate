package com.nut.servcice;


import com.nut.domain.dto.DataBaseSourceDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * @author fym
 * @date 2022/11/28 18:05
 * @email 3271758240@qq.com
 */
public interface GenerateTableService {

    void generateExcel(DataBaseSourceDTO dto, HttpServletResponse response);


    void generateWord(DataBaseSourceDTO dto, HttpServletResponse response);
}
