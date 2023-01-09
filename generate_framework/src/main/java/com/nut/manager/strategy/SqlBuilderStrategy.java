package com.nut.manager.strategy;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.nut.core.build.MySQLDialect;
import com.nut.core.build.OracleDialect;
import com.nut.core.build.SQLDialect;
import com.nut.core.build.SQLServerDialect;
import com.nut.domain.Field;
import com.nut.domain.dto.SqlTableParams;
import com.nut.enums.FieldTypeEnum;
import com.nut.manager.facade.GenerateFacade;
import com.nut.manager.factory.SQLDialectFactory;
import com.nut.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * sql构建管理
 * @author fym
 * @date 2023/1/8 11:38
 * @email 3271758240@qq.com
 */
public class SqlBuilderStrategy {

    private static final Log log = LogFactory.get(SqlBuilderStrategy.class);

    private static SQLDialect sqlDialect;


    public SqlBuilderStrategy(String dialectType) {
        if (StringUtils.isNull(dialectType)){
            this.sqlDialect = SQLDialectFactory.getSQLDialect(OracleDialect.class.getName());
        }
        if (dialectType.equalsIgnoreCase("oracle")){
            this.sqlDialect = SQLDialectFactory.getSQLDialect(OracleDialect.class.getName());
        }else if (dialectType.equalsIgnoreCase("mysql")){
            this.sqlDialect = SQLDialectFactory.getSQLDialect(MySQLDialect.class.getName());
        }else if (dialectType.equalsIgnoreCase("sqlserver")){
            this.sqlDialect = SQLDialectFactory.getSQLDialect(SQLServerDialect.class.getName());
        }

    }

    public SqlBuilderStrategy(SQLDialect sqlDialect){
        this.sqlDialect = sqlDialect;
    }

    /**
     * 设置方言
     *
     * @param sqlDialect
     */
    public void setSqlDialect(SQLDialect sqlDialect) {
        this.sqlDialect = sqlDialect;
    }


    /**
     *
     * @param sqlTableParams
     * @return
     * example:
     * mysql
     * -- prefixComment
     * CREATE TABLE IF NOT EXISTS databaseName.tableName
     * (
     *  `fieldName` fieldType(fieldTypeSize) default value notNull comment ''
     * ) comment suffixComment
     * oracle
     *
     */
    public String buildCreateTableSql(SqlTableParams sqlTableParams){
        // 构建sql模板
        String sqlTemplate = "%s\n" +
                "CREATE TABLE IF NOT EXISTS %s\n" +
                "(\n" +
                "%s\n" +
                ") %s;";
        // 表名称
        String tableName = sqlDialect.wrapTableName(sqlTableParams.getTableName());
        // 数据库名称
        String databaseName = sqlTableParams.getDatabaseName();
        if (StringUtils.isNotNull(databaseName)) {
            tableName = String.format("%s.%s",databaseName,tableName);
        }

        // 表注释
        String tableComment = sqlTableParams.getTableComment();
        if (StringUtils.isNull(tableComment)) {
            tableComment = tableName;
        }
        String prefixComment = String.format("-- %s",tableComment);
        String suffixComment = String.format("COMMENT '%s'", tableComment);
        // 构造表的字段
        Map<String, Field> repeatFieldListMap = new HashMap<>();
        List<Field> fieldList = sqlTableParams.getFieldList();
        // 重复字段集合
        List<String> repeatList = fieldList.stream().collect(Collectors.groupingBy(Field -> Field.getFieldName(), Collectors.counting()))
                .entrySet().stream().filter(e -> e.getValue() > 1).map(Map.Entry::getKey).collect(Collectors.toList());
        StringBuilder fieldBuilder = new StringBuilder();
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            Field repeatField = repeatFieldListMap.get(field.getFieldName());
            if (Objects.nonNull(repeatField)){
                log.info("有重复字段"+repeatField.getFieldName());
            }else {
                fieldBuilder.append(builderFieldSql(field,repeatFieldListMap));
                if (i != fieldList.size() - repeatList.size()) {
                    fieldBuilder.append(",");
                    fieldBuilder.append("\n");
                }
            }

        }
        String fieldStr = fieldBuilder.toString();
        String sql = String.format(sqlTemplate, prefixComment, tableName, fieldStr, suffixComment);
        return sql;
    };

    /**
     * 构建字段sql
     * @param field
     * @return
     */
    public String builderFieldSql(Field field,Map<String, Field> repeatFieldListMap){
        Objects.requireNonNull(field);
        String fieldName = sqlDialect.wrapFieldName(field.getFieldName());
        repeatFieldListMap.put(field.getFieldName(),field);
        String fieldType = field.getFieldType();
        String defaultValue = field.getDefaultValue();
        String comment = field.getComment();
        String onUpdate = field.getOnUpdate();
        Integer fieldTypeSize = field.getFieldTypeSize();
        boolean notNull = field.isNotNull();
        boolean autoIncrement = field.isAutoIncrement();
        boolean primaryKey = field.isPrimaryKey();
        StringBuilder fieldStrBuilder = new StringBuilder();
        // 拼接字段名
        fieldStrBuilder.append(fieldName);
        // 拼接类型
        fieldStrBuilder.append(" ").append(fieldType);
        // 拼接类型
        if (StringUtils.isNotNull(fieldTypeSize) && fieldTypeSize != 0) {
            fieldStrBuilder.append(String.format("(%s)",fieldTypeSize));
        }
        // 拼接默认值
        if (StringUtils.isNotNull(defaultValue)) {
            fieldStrBuilder.append(" ").append("DEFAULT ").append(getValue(field,defaultValue));
        }
        // 拼接是否为空
        fieldStrBuilder.append(" ").append(notNull ? "NOT NULL" : "NULL");
        // 添加更新时间
        if (StringUtils.isNotNull(onUpdate)) {
            fieldStrBuilder.append(" ").append("ON UPDATE ").append(onUpdate);
        }
        // 添加注释
        if (StringUtils.isNotNull(comment)) {
            fieldStrBuilder.append(" ").append(String.format("COMMENT '%s'", comment));
        }
        // 主键
        if (primaryKey) {
            fieldStrBuilder.append(" ").append("PRIMARY KEY");
        }
        // 拼接是否递增
        if (autoIncrement) {
            fieldStrBuilder.append(" ").append("AUTO_INCREMENT");
        }
        return fieldStrBuilder.toString();
    }


    public String getValue(Field field,Object value){
        if (field == null || value == null) {
            return "''";
        }
        FieldTypeEnum fieldTypeEnum = Optional.ofNullable(FieldTypeEnum.getEnumByValue(field.getFieldType())).orElse(FieldTypeEnum.TEXT);
        String result = String.valueOf(value);
        switch (fieldTypeEnum) {
            case DATETIME:
            case TIMESTAMP:
            case DATE:
            case TIME:
            case CHAR:
            case VARCHAR:
            case TINYTEXT:
            case TEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case BINARY:
            case VARBINARY:
                return result.equalsIgnoreCase("CURRENT_TIMESTAMP") ? result : String.format("'%s'", value);
            default:
                return result;
        }

    }

}
