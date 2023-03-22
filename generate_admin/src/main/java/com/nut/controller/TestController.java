package com.nut.controller;

import cn.hutool.http.HttpRequest;
import com.nut.utils.AjaxResult;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author fym
 * @date 2023/2/13 22:07
 * @email 3271758240@qq.com
 */
@RestController
public class TestController {


    @RequestMapping(value = "/test/{data}")
    public AjaxResult test(@PathVariable String data){
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik1UaEVOVUpHTkVNMVFURTRNMEZCTWpkQ05UZzVNRFUxUlRVd1FVSkRNRU13UmtGRVFrRXpSZyJ9.eyJodHRwczovL2FwaS5vcGVuYWkuY29tL3Byb2ZpbGUiOnsiZW1haWwiOiJudXRtaW4uZnVAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImdlb2lwX2NvdW50cnkiOiJVUyJ9LCJodHRwczovL2FwaS5vcGVuYWkuY29tL2F1dGgiOnsidXNlcl9pZCI6InVzZXItRXpaRFpMSnMxTGhtT0VIc1BDUHg5QUt4In0sImlzcyI6Imh0dHBzOi8vYXV0aDAub3BlbmFpLmNvbS8iLCJzdWIiOiJhdXRoMHw2M2E4NWFhMzRmODIzNzZhZDc3NzAzZjQiLCJhdWQiOlsiaHR0cHM6Ly9hcGkub3BlbmFpLmNvbS92MSIsImh0dHBzOi8vb3BlbmFpLm9wZW5haS5hdXRoMGFwcC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNjc1ODYyMTc0LCJleHAiOjE2NzY0NjY5NzQsImF6cCI6IlRkSkljYmUxNldvVEh0Tjk1bnl5d2g1RTR5T282SXRHIiwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSBlbWFpbCBtb2RlbC5yZWFkIG1vZGVsLnJlcXVlc3Qgb3JnYW5pemF0aW9uLnJlYWQgb2ZmbGluZV9hY2Nlc3MifQ.j5DOemZkhmzL_MuO91Ien3hYC4QyVjxU28pLKNycZo-y8TgqLUkvO04A_nuKrh7ja3rNm1K9zt_3Bx8-KKIbWUcC7NLNyOlCs5Z95j89n3NJqGXVAGIohkl9KgnyvPr10sBVJHD7EeeCg8v9dOaZ6xRYy3_db4W0GT4Gj7BF3-9b6xi4OpA-dS7bEk3hxtiiWlAe_5JbfNMoVj7Q8nNseDPqJ2eeeifHXvwZNb0ZO8h0jq11sPyHrBRc4SFIzgCGsSlSHdHTS_Fadek8VhE5CKjHhn7EMeMoS0zAIVHSJueWeXlSCxj7w4lQOHvZpjy30JJqBkrvJP7Z-hZkv99ldw");
        headers.put("Content-Type","application/json");
        String requestJson = String.format("{\n" +
                "    \"model\": \"text-davinci-003\",\n" +
                "     \"prompt\": \"%s\",\n" +
                "      \"temperature\": 0, \n" +
                "      \"max_tokens\": 2048\n" +
                "}",data);

        String body = HttpRequest.post("https://api.openai.com/v1/completions").addHeaders(headers).body(requestJson).execute().body();
        System.out.println(body);


        return AjaxResult.success(body);
    }


    @RequestMapping(value = "/test/gen")
    public AjaxResult gen(){
        Map<String, String> dataMap = new LinkedHashMap<>();
//        Properties properties = new Properties();
//        // 加载classpath目录下的vm文件
//        properties.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//        // 定义字符集
//        properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("message","你好");
        velocityContext.put("time","2023");
        StringWriter sw = new StringWriter();
        Template template = velocityEngine.getTemplate("vm/test.html.vm");
//        Template template = Velocity.getTemplate("test.html.vm");
        template.merge(velocityContext,sw);
        dataMap.put("vm/java/test.html.vm", sw.toString());
        return AjaxResult.success(dataMap);
    }


}
