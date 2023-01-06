package com.nut.utils.http;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author fym
 * @date 2023/1/5 16:52
 * @email 3271758240@qq.com
 */
public class HttpUtils {

    private static final Log log = LogFactory.get(HttpUtils.class);

    public static String getBody(ServletRequest request){
        StringBuilder sb = new StringBuilder();
        // 接受数据避免乱码
        BufferedReader reader = null;
        try (InputStream inputStream = request.getInputStream()) {

            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine())!=null) {
                sb.append(line);
            }

        } catch (IOException e) {
            log.error("getBody出现问题");
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        return sb.toString();
    }
}
