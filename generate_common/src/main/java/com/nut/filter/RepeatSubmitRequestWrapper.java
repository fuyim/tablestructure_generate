package com.nut.filter;

import cn.hutool.http.HttpUtil;
import com.nut.constant.ParameterConstant;
import com.nut.utils.http.HttpUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 参考若依
 * @author fym
 * @date 2023/1/5 16:46
 * @email 3271758240@qq.com
 */
public class RepeatSubmitRequestWrapper extends HttpServletRequestWrapper {

    private final String body;

    public RepeatSubmitRequestWrapper(HttpServletRequest request, ServletResponse response) throws IOException {
        super(request);
        request.setCharacterEncoding(ParameterConstant.UTF8);
        response.setCharacterEncoding(ParameterConstant.UTF8);
        body = HttpUtils.getBody(request);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream(){

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int available() throws IOException {
                return body.getBytes().length;
            }
        };

    }

    public String getBody(){
        return this.body;
    }
}
