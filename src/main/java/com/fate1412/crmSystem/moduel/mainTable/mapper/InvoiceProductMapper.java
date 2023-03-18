package com.fate1412.crmSystem.moduel.mainTable.mapper;

import com.fate1412.crmSystem.moduel.mainTable.pojo.InvoiceProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.moduel.mainTable.pojo.OrderProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 发货单产品 Mapper 接口
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Mapper
public interface InvoiceProductMapper extends BaseMapper<InvoiceProduct> {


    List<OrderProduct> getUnInvoiceNum(@Param("salesOrderId") Long salesOrderId);
    
    List<OrderProduct> getOrderProductList(@Param("salesOrderId") Long salesOrderId, @Param("productId") Long productId);
    
    List<InvoiceProduct> getInvoiceProductList(@Param("salesOrderId") Long salesOrderId, @Param("invoiceIds") List<Long> invoiceIds);
}
