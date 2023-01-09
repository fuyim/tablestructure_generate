package com.nut.core.build;

/**
 * mysql方言
 * @author fym
 * @date 2023/1/8 16:16
 * @email 3271758240@qq.com
 */
public class MySQLDialect implements SQLDialect{

    /**
     * 包装表名
     * @param tableName 表名
     * @return
     */
    @Override
    public String wrapTableName(String tableName) {
        return String.format("`%s`",tableName);
    }

    /**
     * 包装字段名称
     * @param fieldName 字段名
     * @return
     */
    @Override
    public String wrapFieldName(String fieldName) {
        return String.format("`%s`",fieldName);
    }
}
