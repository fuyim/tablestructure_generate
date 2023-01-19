package com.nut.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author fym
 * @date 2023/1/17 17:11
 * @email 3271758240@qq.com
 */
@Configuration
@MapperScan("com.nut.**.mapper")
public class MyBatisConfig {
}
