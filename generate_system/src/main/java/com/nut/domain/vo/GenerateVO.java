package com.nut.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fym
 * @date 2023/1/8 11:21
 * @email 3271758240@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String createSql;
}
