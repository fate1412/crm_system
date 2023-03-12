package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.mainTable.dto.insert.SalesOrderInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.update.SalesOrderUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.SalesOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;

/**
 * <p>
 * 销售订单 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface ISalesOrderService extends IService<SalesOrder>, MyBaseService<SalesOrder> {
    JsonResult<?> updateByDTO(SalesOrderUpdateDTO salesOrderUpdateDTO);
    
    JsonResult<?> addDTO(SalesOrderInsertDTO salesOrderInsertDTO);
    
    boolean delById(Long id);
}
