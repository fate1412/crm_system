package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.mainTable.dto.insert.OrderProductInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.OrderProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.OrderProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.OrderProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.TableResultData;

import java.util.List;

/**
 * <p>
 * 订单产品 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IOrderProductService extends IService<OrderProduct>, MyBaseService<OrderProduct> {
    
    JsonResult<?> updateByDTO(OrderProductUpdateDTO orderProductUpdateDTO);
    
    JsonResult<?> addDTO(OrderProductInsertDTO orderProductInsertDTO);
    
    List<OrderProductSelectDTO> getBySalesOrder(Long salesOrderId);
    
    <D> TableResultData getColumns(D dto);
    
    JsonResult<?> updateByEntity(OrderProduct orderProduct);
    
    JsonResult<?> addEntity(OrderProduct orderProduct);
}
