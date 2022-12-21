package com.nut.servcice.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.nut.constant.ParameterConstant;
import com.nut.domain.dto.DataBaseSourceDTO;
import com.nut.servcice.GenerateTableService;
import com.nut.utils.DataBaseUtils;
import lombok.SneakyThrows;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.*;
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

    @Autowired
    private XWPFDocument document;

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
            if (writer != null) {
                writer.close();
            }
        }
        IoUtil.close(outputStream);
        DynamicDataSourceContextHolder.clear();
    }

    @Override
    public void generateWord(DataBaseSourceDTO dto, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            // 添加数据源
            addDataSource(dto);
            DynamicDataSourceContextHolder.push(dto.getDatabaseName());
            List<String> title = CollUtil.newArrayList("字段名称", "字段类型", "是否为空", "描述");
            // 所需遍历的数据
            List<String> rows = new ArrayList<>();
            Connection connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            metaData.isReadOnly();

            // 获取当前时间
            long currentTime = System.currentTimeMillis();
            String wordName = dto.getDatabaseName() + currentTime;

            // 获取所有表
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});

            // 获取当前表的总列数
            int sumColumnsSize = 0;
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                // 添加一个段落
                XWPFParagraph describeParagraph = document.createParagraph();
                XWPFRun tableDescribe = describeParagraph.createRun();
                describeParagraph.setIndentationFirstLine(400);
                tableDescribe.setFontFamily("宋体");
                tableDescribe.setFontSize(10.5);
                tableDescribe.setText("表" + tableName + "的表结构如下:");
                // 获取表的字段
                ResultSet tableColumns = metaData.getColumns(null, "%", tableName, "%");
                // 获取字段值
                while (tableColumns.next()){
                    String columnName = tableColumns.getString("COLUMN_NAME");
                    String typeName = tableColumns.getString("TYPE_NAME");
                    int columnSize = tableColumns.getInt("COLUMN_SIZE");
                    String type = typeName + "(" + columnSize + ")";
                    String isNULL = "";
                    int nullable = tableColumns.getInt("NULLABLE");
                    if (nullable == 0) {
                        isNULL = "否";
                    } else {
                        isNULL = "是";
                    }
                    String remarks = tableColumns.getString("REMARKS");
                    rows.add(columnName);
                    rows.add(type);
                    rows.add(isNULL);
                    rows.add(remarks);
                    sumColumnsSize++;
                }
                // 创建表格行（获取的表列数+1），列（4）
                XWPFTable table = document.createTable(sumColumnsSize + 1, 4);
                XWPFParagraph tableParagraph = document.createParagraph();
                XWPFRun tableRun = tableParagraph.createRun();
                table.setWidth("100%");
                CTTblPr tblPr = table.getCTTbl().getTblPr();
                CTString tblStyle = tblPr.addNewTblStyle();
                table.setTableAlignment(TableRowAlign.LEFT);
                tblStyle.setVal("表"+tableName+"的样式");
                // 当前行
                int currentRow = 0;
                int currentIndex = 0;
                // 获取表总行
                List<XWPFTableRow> tableRows = table.getRows();
                for (XWPFTableRow tableRow : tableRows) {

                    CTTrPr ctTrPr = tableRow.getCtRow().addNewTrPr();
                    CTHeight ctHeight = ctTrPr.addNewTrHeight();
                    ctHeight.setVal(BigInteger.valueOf(200));

                    List<XWPFTableCell> tableCells = tableRow.getTableCells();
                    for (int i = 0; i < tableCells.size(); i++) {
                        XWPFTableCell cell = tableCells.get(i);
                        CTTcPr ctTcPr = cell.getCTTc().addNewTcPr();
                        CTVerticalJc ctVerticalJc = ctTcPr.addNewVAlign();
                        // 让每一个单元格居中
                        ctVerticalJc.setVal(STVerticalJc.CENTER);
                        CTShd ctShd = ctTcPr.addNewShd();
                        ctShd.setVal(STShd.CLEAR);

                        XWPFParagraph xwpfParagraph = null;
                        int size = cell.getParagraphs().size();
                        if (size == 0){
                            xwpfParagraph = cell.addParagraph();
                        }else {
                            xwpfParagraph = cell.getParagraphs().get(size - 1);
                        }
                        XWPFRun cellRun = xwpfParagraph.createRun();
                        cellRun.setFontSize(10.5);
                        cellRun.setFontFamily("宋体");
                        // 设置第一行字体加粗
                        if (currentRow == 0){
                            cellRun.setBold(true);
                            cellRun.setText(title.get(i));
                        }
                        else {
                            cellRun.setText(rows.get(currentIndex));
                            currentIndex++;
                        }
                        xwpfParagraph.setAlignment(ParagraphAlignment.CENTER);
                    }
                    currentRow++;
                }
                sumColumnsSize  = 0;
                tableRun.addBreak();
                rows.clear();

            }
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=UTF-8");
            response.setHeader("Content-Disposition", StrUtil.format("attachment;filename={}.docx", wordName));
            outputStream = response.getOutputStream();
            document.write(outputStream);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

