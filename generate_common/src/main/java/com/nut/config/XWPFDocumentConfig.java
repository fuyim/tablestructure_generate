package com.nut.config;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fym
 * @date 2022/12/20 19:45
 * @email 3271758240@qq.com
 */

@Configuration
public class XWPFDocumentConfig {

    @Bean
    public XWPFDocument getXWPFDocument(){
        return new XWPFDocument();
    }

}
