package com.fate1412.crmSystem.module.customTable.service;

import com.fate1412.crmSystem.module.customTable.dto.select.OptionDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableOptionSelectDTO;
import com.fate1412.crmSystem.module.customTable.pojo.TableOption;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.TableResultData;

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
    
    List<TableOptionSelectDTO> getDTOByTableColumnId(Integer id);
    
    <D> TableResultData getColumns(D dto);
    
    boolean delAllOption(String tableName);
}
