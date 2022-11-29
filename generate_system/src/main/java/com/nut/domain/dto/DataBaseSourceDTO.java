package com.nut.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author fym
 * @date 2022/11/25 18:15
 * @email 3271758240@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataBaseSourceDTO implements Serializable {


    private static final long serialVersionUID = 1L;
    /**
     * JDBC 用户名
     */

    @NotBlank(message = "用户名称不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * JDBC url 地址
     */
    private String url;

    private String driverClassName;

    private String poolName;

    /**
     * JDBC 主机号
     */
    @NotBlank(message = "主机号不能为空")
    private String hostname;

    @NotBlank(message = "数据库名称不能为空")
    private String databaseName;

}
