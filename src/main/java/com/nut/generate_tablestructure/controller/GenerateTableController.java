package com.nut.generate_tablestructure.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author fym
 * @date 2022/11/23 12:35
 * @email 3271758240@qq.com
 */
@Controller
@Log4j2
public class GenerateTableController {


    @Autowired
    private DataSource dataSource;

    @Value("${spring.constant.upload-path}")
    private String uploadPath;

    private Map<String,String> map = new HashMap<>(8);


    @RequestMapping("/")
    public String goHome() throws SQLException {
        return "index";
    }


    @RequestMapping("/generateExcel")
    @ResponseBody
    public void generateWord() throws SQLException {
        List<String> title = CollUtil.newArrayList("字段名称", "字段类型", "是否为空", "描述");
        List <String> rows = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        metaData.isReadOnly();
        ResultSet tables = metaData.getTables(connection.getCatalog(), null, "t_%", new String[]{"TABLE"});
        ResultSet columns = null;


        ExcelWriter writer = ExcelUtil.getWriter(uploadPath+"test.xlsx");

        while(tables.next()){
            String tableName = tables.getString("TABLE_NAME");
            writer.merge(3,tableName);
            writer.autoSizeColumnAll();
            writer.writeRow(title);
            columns = metaData.getColumns(null, "%", tableName, "%");
            while (columns.next()){
                String columnName = columns.getString("COLUMN_NAME");
                String typeName = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                String type = typeName+"("+columnSize+")";
                String isNULL = "";
                int nullable = columns.getInt("NULLABLE");
                if (nullable == 0){
                    isNULL = "否";
                }else {
                    isNULL = "是";
                }
                String remarks = columns.getString("REMARKS");
                rows.add(columnName);
                rows.add(type);
                rows.add(isNULL);
                rows.add(remarks);

                writer.writeRow(rows, true);
                rows.clear();
            }
        }
        writer.close();
    }

}
