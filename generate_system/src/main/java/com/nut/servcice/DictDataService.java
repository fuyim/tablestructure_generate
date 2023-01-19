package com.nut.servcice;

import com.nut.domain.DictData;

import java.util.List;

/**
 * @author fym
 * @date 2023/1/17 16:38
 * @email 3271758240@qq.com
 */
public interface DictDataService {

    List<DictData> selectDictDataByType(String dictType);

}
