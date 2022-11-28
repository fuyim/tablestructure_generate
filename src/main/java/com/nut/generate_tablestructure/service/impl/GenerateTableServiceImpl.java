package com.nut.generate_tablestructure.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.nut.generate_tablestructure.constant.ParameterConstant;
import com.nut.generate_tablestructure.dto.DataBaseSourceDTO;
import com.nut.generate_tablestructure.service.GenerateTableService;
import com.nut.generate_tablestructure.utils.DataBaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fym
 * @date 2022/11/28 18:06
 * @email 3271758240@qq.com
 */
@Service
public class GenerateTableServiceImpl implements GenerateTableService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DataSourceProperty dataSourceProperty;

    @Autowired
    private DefaultDataSourceCreator dataSourceCreator;

    @Override
    public void generateExcel(DataBaseSourceDTO dto, HttpServletResponse response) {
        ResultSet columns = null;
        ExcelWriter writer = null;
        ServletOutputStream outputStream = null;
        try {
            // 添加数据源
            addDataSource(dto);
            DynamicDataSourceContextHolder.push(dto.getDatabaseName());
            List<String> title = CollUtil.newArrayList("字段名称", "字段类型", "是否为空", "描述");
            List<String> rows = new ArrayList<>();
            Connection connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            metaData.isReadOnly();
            // 获取所有表
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});
            // 获取当前时间
            long currentTime = System.currentTimeMillis();
            String excelName = dto.getDatabaseName() + currentTime;
            writer = ExcelUtil.getWriter(true);

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                writer.merge(3, tableName);
                writer.autoSizeColumnAll();
                writer.writeRow(title);
                columns = metaData.getColumns(null, "%", tableName, "%");
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    String typeName = columns.getString("TYPE_NAME");
                    int columnSize = columns.getInt("COLUMN_SIZE");
                    String type = typeName + "(" + columnSize + ")";
                    String isNULL = "";
                    int nullable = columns.getInt("NULLABLE");
                    if (nullable == 0) {
                        isNULL = "否";
                    } else {
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
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", StrUtil.format("attachment;filename={}.xlsx", excelName));
            outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer !=null){
                writer.close();
            }
        }
        IoUtil.close(outputStream);
        DynamicDataSourceContextHolder.clear();
    }

    public void addDataSource(DataBaseSourceDTO dto) {
        String mysqlUrl = DataBaseUtils.getMysqlUrl(dto.getDatabaseName(), dto.getHostname());
        dto.setUrl(mysqlUrl);
        dto.setDriverClassName(ParameterConstant.DRIVER_CLASS_NAME);
        dto.setPoolName(ParameterConstant.POOL_NAME);
        BeanUtils.copyProperties(dto, dataSourceProperty);
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource newDataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        ds.addDataSource(dto.getDatabaseName(), newDataSource);
    }
}
