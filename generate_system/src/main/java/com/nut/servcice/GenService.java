package com.nut.servcice;

import com.nut.domain.dto.SqlTableParams;

import java.util.Map;

/**
 * @author fym
 * @date 2023/3/15 18:23
 * @email 3271758240@qq.com
 */
public interface GenService {

    public Map<String,String> genPreviewCode(SqlTableParams sqlTableParams);

}
