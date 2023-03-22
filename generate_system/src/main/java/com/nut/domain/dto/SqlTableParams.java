package com.nut.domain.dto;

import com.nut.domain.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 表所需参数
 * @author fym
 * @date 2023/1/7 22:02
 * @email 3271758240@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlTableParams implements Serializable{

    private static final long serialVersionUID = 1L;

    // 数据库名称
    private String databaseName;

    // 表名称
    @NotBlank(message = "表名称不能为空")
    private String tableName;

    // 表描述
    private String tableComment;

    // 字段列表
    @Valid
    @Size(min = 1, message = "字段至少有一个")
    private List<Field> fieldList;

    // 包名
    private String packageName;
}
