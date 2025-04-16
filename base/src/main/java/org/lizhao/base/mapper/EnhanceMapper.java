package org.lizhao.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;

public interface EnhanceMapper<T> extends BaseMapper<T> {
    /**
     * 真正的批量插入
     * @param entityList 实体列表
     * @return 插入数据量
     */
    int insertBatchSomeColumn(List<T> entityList);
}
