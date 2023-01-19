package com.nut.mapper;

import com.nut.domain.DictData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fym
 * @date 2023/1/17 16:50
 * @email 3271758240@qq.com
 */
@Repository
public interface DictDataMapper {

    List<DictData> selectDictDataByType(String dictType);

}
