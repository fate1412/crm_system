package com.fate1412.crmSystem.mainTable.mapper;

import com.fate1412.crmSystem.mainTable.pojo.InvoiceProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.mainTable.pojo.OrderProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

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
