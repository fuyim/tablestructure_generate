package com.nut.servcice.impl;

import com.nut.domain.Field;
import com.nut.domain.dto.SqlTableParams;
import com.nut.enums.FieldTypeEnum;
import com.nut.servcice.GenService;
import com.nut.utils.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.sql.Time;
import java.util.*;

/**
 * @author fym
 * @date 2023/3/15 18:24
 * @email 3271758240@qq.com
 */
@Service
public class GenServiceImpl implements GenService {

    @Autowired
    private VelocityEngine velocityEngine;




    @Override
    public Map<String, String> genPreviewCode(SqlTableParams sqlTableParams) {

        Map<String, String> dataMap = new LinkedHashMap<>();

        // Velocity初始化
        VelocityContext vc = velocityContext(sqlTableParams);
        List<String> velocityTemplate = getVelocityTemplate();
        for (String template : velocityTemplate) {
            StringWriter sw = new StringWriter();
            Template tpl = velocityEngine.getTemplate(template);
            tpl.merge(vc,sw);
            template = template.substring(template.lastIndexOf("/")+1,template.indexOf("."));
            dataMap.put(template,sw.toString());
        }
        return dataMap;
    }


    public VelocityContext velocityContext(SqlTableParams sqlTableParams){

        VelocityContext velocityContext = new VelocityContext();
        // 构建参数
        List<Field> fields = buildJavaFields(sqlTableParams.getFieldList());
        // 根据类型获取导入的包
        HashSet<String> importList = getImportPackageList(sqlTableParams.getFieldList());
        velocityContext.put("fields",fields);
        String className = sqlTableParams.getTableName();
        velocityContext.put("className",className);
        velocityContext.put("importList",importList);
        String tableName = sqlTableParams.getTableName();
        velocityContext.put("tableName",tableName);
        String tableComment = sqlTableParams.getTableComment();
        velocityContext.put("tableComment",tableComment);
        String packageName = sqlTableParams.getPackageName();
        if (StringUtils.isEmpty(packageName)) {
            packageName = "com.nut";
        }
        velocityContext.put("packageName",packageName);
        return velocityContext;
    }

    // 构建字段
    public List<Field> buildJavaFields(List<Field> fields){

        for (Field field : fields) {
            FieldTypeEnum fieldTypeEnum = Optional.ofNullable(FieldTypeEnum.getEnumByValue(field.getFieldType())).orElse(FieldTypeEnum.TEXT);
            field.setJavaType(fieldTypeEnum.getJavaType());
        }

        return fields;
    }

    public HashSet<String> getImportPackageList(List<Field> fields){
        HashSet<String> importList = new HashSet<>();
        for (Field field : fields) {
            FieldTypeEnum fieldTypeEnum = Optional.ofNullable(FieldTypeEnum.getEnumByValue(field.getFieldType())).orElse(FieldTypeEnum.TEXT);
            // date类型
            if (FieldTypeEnum.DATE.getJavaType().equals(fieldTypeEnum.getJavaType())) {
                importList.add("java.util.Date");
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
            } else if (FieldTypeEnum.DECIMAL.getJavaType().equals(fieldTypeEnum.getJavaType())){
                importList.add("java.math.BigDecimal");
            }

        }
        return importList;
    }


    /**
     * 获取velocity模板
     */
    public List<String> getVelocityTemplate(){
        List<String> velocityList = new ArrayList<>();
        velocityList.add("vm/java/domain.java.vm");
        velocityList.add("vm/java/mapper.java.vm");
        velocityList.add("vm/java/service.java.vm");
        velocityList.add("vm/java/serviceImpl.java.vm");
        velocityList.add("vm/java/controller.java.vm");
        return velocityList;
    }



}
