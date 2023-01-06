package com.nut.config;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.io.unit.DataUnit;
import com.nut.annotation.RepeatSubmit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 缓存配置
 * @author fym
 * @date 2023/1/6 16:46
 * @email 3271758240@qq.com
 */
@Configuration
public class CacheConfig {

    @Bean
    public TimedCache<String, Object> getCache(){

        return CacheUtil.newTimedCache(DateUnit.SECOND.getMillis()*1000);
    }

}
