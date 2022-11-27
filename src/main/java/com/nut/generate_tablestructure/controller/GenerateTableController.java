package com.nut.generate_tablestructure.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.nut.generate_tablestructure.constant.ParameterConstant;
import com.nut.generate_tablestructure.dto.DataBaseSourceDTO;
import com.nut.generate_tablestructure.utils.AjaxResult;
import com.nut.generate_tablestructure.utils.DataBaseUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.sql.*;
import java.util.*;

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
    private DataSource dataSource;

    @Autowired
    private DataSourceProperty dataSourceProperty;

    @Autowired
    private DefaultDataSourceCreator dataSourceCreator;


    @Value("${spring.constant.upload-path}")
    private String uploadPath;

    private Map<String,String> map = new HashMap<>(8);


    /**
     * 根据目标数据源生成excel
     */
    @RequestMapping(value = "/generateExcel",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult generateExcel(@Valid @RequestBody DataBaseSourceDTO dto, BindingResult result) throws SQLException {

        if (result.hasErrors()){
            result.getFieldErrors().forEach(item->{
                String message = item.getDefaultMessage();
                String field = item.getField();
                map.put(field,message);
            });
            return AjaxResult.success(ParameterConstant.error,map);
        }else {
            // 添加数据源
            addDataSource(dto);
            DynamicDataSourceContextHolder.push(dto.getDatabaseName());
            List<String> title = CollUtil.newArrayList("字段名称", "字段类型", "是否为空", "描述");
            List <String> rows = new ArrayList<>();
            Connection connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            metaData.isReadOnly();
            // 获取所有表
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});
            ResultSet columns = null;
            // 获取当前时间
            long currentTime = System.currentTimeMillis();
            String excelName = dto.getDatabaseName() + currentTime + ".xlsx";
            System.out.println(excelName);
            ExcelWriter writer = ExcelUtil.getWriter(uploadPath+excelName);

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
            DynamicDataSourceContextHolder.clear();
            return AjaxResult.success();
        }
    }


    public void addDataSource(DataBaseSourceDTO dto){
        String mysqlUrl = DataBaseUtils.getMysqlUrl(dto.getDatabaseName(), dto.getHostname());
        dto.setUrl(mysqlUrl);
        dto.setDriverClassName(ParameterConstant.DRIVER_CLASS_NAME);
        dto.setPoolName(ParameterConstant.POOL_NAME);
        BeanUtils.copyProperties(dto,dataSourceProperty);
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource newDataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        ds.addDataSource(dto.getDatabaseName(),newDataSource);
    }



}
