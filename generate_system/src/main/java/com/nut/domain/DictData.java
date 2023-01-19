package com.nut.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fym
 * @date 2023/1/16 21:47
 * @email 3271758240@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictData implements Serializable {

    private static final long serialVersionUID = 1L;


    // 字典编码
    private Long dictCode;

    // 字典排序
    private Long dictSort;

    // 字典标签
    private String dictLabel;

    // 字典键值
    private String dictValue;

    // 字典类型
    private String dictType;

    // 样式属性
    private String cssClass;

    // 表格字典样式
    private String listClass;

    // 是否默认（Y是 N否）
    private String isDefault;

    // 状态（0正常 1停用）
    private String status;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private String remark;
}
