package com.fate1412.crmSystem.mainTable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.dto.OrderProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.OrderProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.OrderProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单产品 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IOrderProductService extends IService<OrderProduct> {
    IPage<OrderProductSelectDTO> listByPage(long thisPage, long pageSize);
    
    List<OrderProductSelectDTO> getDTOListById(List<Long> ids);
    
    boolean updateById(OrderProductUpdateDTO orderProductUpdateDTO);
    
    boolean add(OrderProductSelectDTO orderProductSelectDTO);
}
