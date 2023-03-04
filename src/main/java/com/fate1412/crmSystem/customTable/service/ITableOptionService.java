package com.fate1412.crmSystem.customTable.service;

import com.fate1412.crmSystem.customTable.pojo.Option;
import com.fate1412.crmSystem.customTable.pojo.TableOption;
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

    List<Option> getOptions(String tableName,String columnName);
}
