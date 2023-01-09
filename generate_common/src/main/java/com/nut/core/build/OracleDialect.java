package com.nut.core.build;

/**
 * oracle方言
 * @author fym
 * @date 2023/1/8 21:33
 * @email 3271758240@qq.com
 */
public class OracleDialect implements SQLDialect{
    @Override
    public String wrapTableName(String tableName) {
        return null;
    }

    @Override
    public String wrapFieldName(String fieldName) {
        return null;
    }
}
