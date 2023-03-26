package com.fate1412.crmSystem.module.customTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.dto.insert.TableColumnInsertDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.update.TableColumnUpdateDTO;
import com.fate1412.crmSystem.module.customTable.pojo.TableColumnDict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;

import java.util.List;

/**
 * <p>
 * 字段字典表 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
public interface ITableColumnDictService extends IService<TableColumnDict>, MyBaseService<TableColumnDict> {
    
    List<TableColumnDict> listByTableName(String tableName);

    List<TableColumnSelectDTO> DTOListByTableName(String tableName);
    
    MyPage listByPage(SelectPage<TableDictSelectDTO> selectPage);
    
    JsonResult<?> addDTO(TableColumnInsertDTO tableColumnInsertDTO);
    
    JsonResult<?> updateByDTO(TableColumnUpdateDTO tableColumnUpdateDTO);
    
    boolean delById(Long id);
}
