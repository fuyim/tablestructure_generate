package com.nut.enums;

import com.nut.utils.StringUtils;

/**
 * 字段枚举
 * @author fym
 * @date 2023/1/8 16:52
 * @email 3271758240@qq.com
 */
public enum FieldTypeEnum {

    TINYINT("tinyint"),
    SMALLINT("smallint"),
    MEDIUMINT("mediumint"),
    INT("int"),
    BIGINT("bigint"),
    FLOAT("float"),
    DOUBLE("double"),
    DECIMAL("decimal"),
    DATE("date"),
    TIME("time"),
    YEAR("year"),
    DATETIME("datetime"),
    TIMESTAMP("timestamp"),
    CHAR("char"),
    VARCHAR("varchar"),
    TINYTEXT("tinytext"),
    TEXT("text"),
    MEDIUMTEXT("mediumtext"),
    LONGTEXT("longtext"),
    TINYBLOB("tinyblob"),
    BLOB("blob"),
    MEDIUMBLOB("mediumblob"),
    LONGBLOB("longblob"),
    BINARY("binary"),
    VARBINARY("varbinary");

    private final String value;


    FieldTypeEnum(String value) {
        this.value = value;
    }

    /**
     * 根据字段类型获取枚举
     * @param fieldType 字段类型
     * @return
     */
    public static FieldTypeEnum getEnumByValue(String fieldType) {
        if (StringUtils.isNotNull(fieldType)) {
            return null;
        }
        for (FieldTypeEnum fieldTypeEnum : FieldTypeEnum.values()) {
            if (fieldTypeEnum.value.equals(fieldType)) {
                return fieldTypeEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

}
