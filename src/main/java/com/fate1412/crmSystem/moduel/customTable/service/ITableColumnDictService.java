package com.fate1412.crmSystem.moduel.customTable.service;

import com.fate1412.crmSystem.moduel.customTable.dto.TableColumnDTO;
import com.fate1412.crmSystem.moduel.customTable.pojo.TableColumnDict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字段字典表 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
public interface ITableColumnDictService extends IService<TableColumnDict> {
    
    List<TableColumnDict> listByTableName(String tableName);

    List<TableColumnDTO> DTOListByTableName(String tableName);
}
