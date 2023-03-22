package com.nut.enums;

import com.nut.utils.StringUtils;

/**
 * 字段枚举
 * @author fym
 * @date 2023/1/8 16:52
 * @email 3271758240@qq.com
 */
public enum FieldTypeEnum {

    TINYINT("tinyint","Integer"),
    SMALLINT("smallint","Integer"),
    MEDIUMINT("mediumint","Integer"),
    INT("int","Integer"),
    BIGINT("bigint","Long"),
    FLOAT("float","Double"),
    DOUBLE("double","Double"),
    DECIMAL("decimal","BigDecimal"),
    DATE("date","Date"),
    TIME("time","Time"),
    YEAR("year","Integer"),
    DATETIME("datetime","Date"),
    TIMESTAMP("timestamp","Long"),
    CHAR("char","String"),
    VARCHAR("varchar","String"),
    TINYTEXT("tinytext","String"),
    TEXT("text","String"),
    MEDIUMTEXT("mediumtext","String"),
    LONGTEXT("longtext","String"),
    TINYBLOB("tinyblob","byte[]"),
    BLOB("blob","byte[]"),
    MEDIUMBLOB("mediumblob","byte[]"),
    LONGBLOB("longblob","byte[]"),
    BINARY("binary","byte[]"),
    VARBINARY("varbinary","byte[]");

    private final String value;

    private final String javaType;


    FieldTypeEnum(String value,String javaType) {
        this.value = value;
        this.javaType = javaType;
    }

    /**
     * 根据字段类型获取枚举
     * @param fieldType 字段类型
     * @return
     */
    public static FieldTypeEnum getEnumByValue(String fieldType) {
        if (StringUtils.isNull(fieldType)) {
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

    public String getJavaType () {
        return javaType;
    }

}
