package com.nut.servcice.impl;

import com.nut.domain.DictData;
import com.nut.mapper.DictDataMapper;
import com.nut.servcice.DictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author fym
 * @date 2023/1/17 16:40
 * @email 3271758240@qq.com
 */
@Service
public class DictDataServiceImpl implements DictDataService {

    @Autowired
    private DictDataMapper dictDataMapper;

    @Override
    public List<DictData> selectDictDataByType(String dictType) {

        List<DictData> dictDataList = dictDataMapper.selectDictDataByType(dictType);
        if (Objects.isNull(dictDataList)){
            dictDataList = new ArrayList<>();
        }
        return dictDataList;
    }
}
