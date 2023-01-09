package com.nut.manager.factory;

import com.nut.core.build.SQLDialect;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 工厂 + 单例
 *
 * @author fym
 * @date 2023/1/8 15:42
 * @email 3271758240@qq.com
 */
public class SQLDialectFactory {

    private static final Map<String, SQLDialect> DIALECT_MAP = new ConcurrentHashMap<>();

    private SQLDialectFactory() {

    }

    /**
     * 根据类名获取方言的实例
     *
     * @param className 类名
     * @return
     */
    public static SQLDialect getSQLDialect(String className) {
        SQLDialect sqlDialect = DIALECT_MAP.get(className);
        if (sqlDialect == null){
            synchronized (className.intern()) {
                sqlDialect = DIALECT_MAP.computeIfAbsent(className,
                    key -> {
                        try {
                            return (SQLDialect) Class.forName(className).newInstance();
                        } catch (Exception e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                );
            }
        }
        return sqlDialect;
    }

}
