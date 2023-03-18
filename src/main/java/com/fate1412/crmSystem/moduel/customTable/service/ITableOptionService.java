package com.fate1412.crmSystem.moduel.customTable.service;

import com.fate1412.crmSystem.moduel.customTable.dto.OptionDTO;
import com.fate1412.crmSystem.moduel.customTable.pojo.TableOption;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字段选择值 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
public interface ITableOptionService extends IService<TableOption> {

    List<OptionDTO> getOptions(String tableName, String columnName);
    
    boolean selectOptions(String tableName, String columnName, Integer key);
}
