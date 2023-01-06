package com.nut.annotation;

import java.lang.annotation.*;

/**后端防止重复提交
 * 使用aop + hutool TimedCache定时缓存 单机
 * @author fym
 * @date 2023/1/5 16:14
 * @email 3271758240@qq.com
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {

    /**
     * 设置间隔时间，小于此间隔视为重复提交
     * @return
     */
    int interval() default 1000;

    String message() default "数据正在处理，请勿重复提交";

}
