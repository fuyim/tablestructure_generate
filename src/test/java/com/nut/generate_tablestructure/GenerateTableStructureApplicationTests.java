package com.nut.generate_tablestructure;

import cn.hutool.core.util.URLUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class GenerateTableStructureApplicationTests {
    @Value("${spring.constant.upload-path}")
    private String uploadPath;
    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() {
        String encode = URLUtil.encode(uploadPath);
        System.out.println(encode);
        System.out.println(uploadPath);
    }


    @Test
    void test() throws Exception {

        List<String> title = new ArrayList<>();
        List<String> rows = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        metaData.isReadOnly();
        ResultSet tables = metaData.getTables(connection.getCatalog(), null, "t_%", new String[]{"TABLE"});
        ResultSet columns = null;
        while(tables.next()){
            String tableName = tables.getString("TABLE_NAME");
            title.add(tableName);
            columns = metaData.getColumns(null, "%", tableName, "%");
            while(columns.next()){
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
            }
        }
    }

}
