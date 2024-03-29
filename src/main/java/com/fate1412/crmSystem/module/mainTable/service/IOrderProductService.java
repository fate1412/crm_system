package com.fate1412.crmSystem.module.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.mainTable.dto.insert.OrderProductInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.OrderProductSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.OrderProductUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.pojo.OrderProduct;
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
    
    List<OrderProductSelectDTO> getDTOBySalesOrderId(Long salesOrderId);
    
    List<OrderProduct> getBySalesOrderId(Long salesOrderId);
    
    <D> TableResultData getColumns(D dto);
    
    JsonResult<?> updateByEntity(OrderProduct orderProduct);
    
    JsonResult<?> addEntity(OrderProduct orderProduct);
    
    boolean delById(Long id);
    
    boolean delByIds(List<Long> ids);
    
    boolean delBySalesOrderId(Long id);
    
    MyPage listByPage(SelectPage<OrderProductSelectDTO> selectPage);
}
