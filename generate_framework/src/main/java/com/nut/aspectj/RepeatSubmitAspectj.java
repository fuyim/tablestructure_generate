package com.nut.aspectj;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.nut.annotation.RepeatSubmit;
import com.nut.constant.ParameterConstant;
import com.nut.filter.RepeatSubmitRequestWrapper;
import com.nut.utils.AjaxResult;
import com.nut.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author fym
 * @date 2023/1/5 16:20
 * @email 3271758240@qq.com
 */
@Aspect
@Component
public class RepeatSubmitAspectj {

    @Autowired
    private TimedCache<String,Object> timedCache;

    private static final Log log = LogFactory.get(RepeatSubmitAspectj.class);

    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";

    @Pointcut("@annotation(repeatSubmit)")
    public void pointCut(RepeatSubmit repeatSubmit) {

    }

    /**
     * 环绕通知 ： 判断表单是否重复提交
     * @param joinPoint
     * @param repeatSubmit
     * @return
     */
    @SuppressWarnings("unchecked")
    @Around("pointCut(repeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint,RepeatSubmit repeatSubmit) {
        String nowBody = "";
        try {

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
            HttpServletRequest request = attributes.getRequest();
            HttpServletResponse response = attributes.getResponse();
            // 获取json格式参数
            RepeatSubmitRequestWrapper requestWrapper = new RepeatSubmitRequestWrapper(request,response);
            nowBody = requestWrapper.getBody();
            // 获取表单格式参数
            if (StringUtils.isEmpty(nowBody)) {
                Map<String, String[]> parameterMap = request.getParameterMap();
                nowBody = JSONUtil.toJsonStr(parameterMap);
            }
             Map<String, Object> nowDataMap = new HashMap<String, Object>();
            nowDataMap.put(REPEAT_PARAMS, nowBody);
            nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

            String url = request.getRequestURI();

            // 唯一标识(指定key + url + sessionID)
            String cacheRepeatKey = ParameterConstant.REPEAT_SUBMIT_KEY + url + sessionId;

            // 获取缓存值
            Object sessionObj = timedCache.get(cacheRepeatKey,false);
            if (!Objects.isNull(sessionObj)) {
                Map<String, Object> sessionMap = (Map<String, Object>) sessionObj;
                if (sessionMap.containsKey(cacheRepeatKey)) {
                    Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(cacheRepeatKey);
                    if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap, repeatSubmit.interval())) {
                        AjaxResult error = AjaxResult.error(repeatSubmit.message());
                        String jsonStr = JSONUtil.toJsonStr(error);
                        ServletUtil.write(response, jsonStr, ParameterConstant.CONTENT_TYPE);
                    }
                }
            } else {
                Map<String, Object> cacheObj = new HashMap<String, Object>();
                cacheObj.put(cacheRepeatKey, nowDataMap);
                timedCache.put(cacheRepeatKey, cacheObj, repeatSubmit.interval());
                Object proceed = joinPoint.proceed();
                return proceed;
            }
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return null;
    }


    /**
     * 对比参数
     * @param nowMap 现有参数
     * @param preMap 缓存参数
     * @return boolean
     */
    public boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap) {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * 对比时间
     * @param nowMap 现有时间
     * @param preMap 缓存时间
     * @param interval 间隔
     * @return boolean
     */
    public boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap, int interval) {
        long nowTime = (Long) nowMap.get(REPEAT_TIME);
        long preTime = (Long) preMap.get(REPEAT_TIME);
        // 请求时间小于间隔时间视为超时
        if ((nowTime - preTime) < interval) {
            return true;
        }
        return false;
    }


}
