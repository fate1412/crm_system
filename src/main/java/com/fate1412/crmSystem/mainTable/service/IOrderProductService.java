package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.mainTable.dto.select.OrderProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.OrderProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.OrderProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;

/**
 * <p>
 * 订单产品 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IOrderProductService extends IService<OrderProduct>, MyBaseService<OrderProduct> {
    
    JsonResult<?> updateById(OrderProductUpdateDTO orderProductUpdateDTO);
    
    JsonResult<?> add(OrderProductSelectDTO orderProductSelectDTO);
}
