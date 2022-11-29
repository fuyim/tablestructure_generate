package com.nut.config;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fym
 * @date 2022/11/25 22:28
 * @email 3271758240@qq.com
 */
@Configuration
public class DataSourceConfig {


    @Bean
    public DataSourceProperty getDataSourceProperty(){
        return new DataSourceProperty();
    }

}
