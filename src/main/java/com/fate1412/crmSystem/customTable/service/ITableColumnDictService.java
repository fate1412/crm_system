package com.fate1412.crmSystem.customTable.service;

import com.fate1412.crmSystem.customTable.dto.TableColumn;
import com.fate1412.crmSystem.customTable.pojo.TableColumnDict;
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

    List<TableColumn> DTOListByTableName(String tableName);
}
