package com.nut.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author fym
 * @date 2023/1/8 10:38
 * @email 3271758240@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Field implements Serializable {

    private static final long serialVersionUID = 1L;

    // 字段名
    @NotBlank(message = "字段名不能为空")
    private String fieldName;

    // 字段类型
    @NotBlank(message = "字段类型不能为空")
    private String fieldType;

    // 字段类型大小
    private Integer fieldTypeSize;

    // 是否为空
    private boolean notNull;

    // 字段描述
    private String comment;

    // 默认值
    private String defaultValue;

    // 主键
    private boolean primaryKey;

    // 自增
    private boolean autoIncrement;

    // 更新时间
    private String onUpdate;

    private String javaType;

    @Override
    public String toString() {
        return "Field{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldType='" + fieldType + '\'' +
                ", fieldTypeSize=" + fieldTypeSize +
                ", notNull=" + notNull +
                ", comment='" + comment + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", primaryKey=" + primaryKey +
                ", autoIncrement=" + autoIncrement +
                ", onUpdate='" + onUpdate + '\'' +
                ", javaType='" + javaType + '\'' +
                '}';
    }

}
