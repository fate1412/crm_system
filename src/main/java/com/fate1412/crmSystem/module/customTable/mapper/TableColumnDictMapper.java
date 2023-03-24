package com.fate1412.crmSystem.module.customTable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.module.customTable.pojo.TableColumnDict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 字段字典表 Mapper 接口
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@Mapper
public interface TableColumnDictMapper extends BaseMapper<TableColumnDict> {

    int insert(List<TableColumnDict> list);
}
