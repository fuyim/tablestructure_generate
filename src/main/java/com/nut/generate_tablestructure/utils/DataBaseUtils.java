package com.nut.generate_tablestructure.utils;

import cn.hutool.core.codec.Base64;
import com.nut.generate_tablestructure.dto.DataBaseSourceDTO;


/**
 * @author fym
 * @date 2022/11/25 17:38
 * @email 3271758240@qq.com
 */
public class DataBaseUtils {

    /**
     * 获取mysql数据库url地址
     * @param databaseName
     * @return
     */
    public static String getMysqlUrl(String databaseName,String hostname){

        String encode = Base64.encode(databaseName);
        String url = "jdbc:mysql://"+hostname+":3306/"+Base64.decodeStr(encode)+"?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&serverTimezone=GMT%2B8";

        return url;

    }



}
