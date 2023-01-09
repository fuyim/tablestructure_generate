package com.nut.core.build;

/**
 * 设置数据库方言
 * @author fym
 * @date 2023/1/8 14:52
 * @email 3271758240@qq.com
 */
public interface SQLDialect {


    String wrapTableName(String tableName);

    String wrapFieldName(String fieldName);
}
