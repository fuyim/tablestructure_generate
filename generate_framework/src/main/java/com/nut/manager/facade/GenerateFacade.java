package com.nut.manager.facade;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.nut.core.build.MySQLDialect;
import com.nut.core.build.SQLDialect;
import com.nut.domain.dto.SqlTableParams;
import com.nut.domain.vo.GenerateVO;
import com.nut.manager.strategy.SqlBuilderStrategy;
import org.springframework.stereotype.Component;

/**
 * 生成门面
 * @author fym
 * @date 2023/1/8 11:16
 * @email 3271758240@qq.com
 */

@Component
public class GenerateFacade {

    private static final Log log = LogFactory.get(GenerateFacade.class);

    /**
     * 生成建表sql
     * @param sqlTableParams
     * @return
     */
    public static GenerateVO generateCreateSql(SqlTableParams sqlTableParams){
        SqlBuilderStrategy builderStrategys = new SqlBuilderStrategy("MySQL");
        String createSql = builderStrategys.buildCreateTableSql(sqlTableParams);
        GenerateVO generateVO = new GenerateVO();
        generateVO.setCreateSql(createSql);
        return generateVO;
    }

}
