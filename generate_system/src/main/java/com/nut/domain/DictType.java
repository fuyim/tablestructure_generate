package com.nut.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fym
 * @date 2023/1/17 16:13
 * @email 3271758240@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictType implements Serializable {

    private static final long serialVersionUID = 1L;

    // 字典主键
    private Long dictId;

    // 字典名称
    private String dictName;

    // 字典类型
    private String dictType;

    // 状态（0正常 1停用）
    private String status;

}
