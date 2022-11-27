package com.nut.generate_tablestructure.utils;

/**
 * @author fym
 * @date 2022/11/27 13:47
 * @email 3271758240@qq.com
 * 字符串工具
 */
public class StringUtils {


    public static <T> Boolean isNull(T o){
        return o == null;
    }

    public static <T> Boolean isNotNull(T o){
        return !isNull(o);
    }
}
