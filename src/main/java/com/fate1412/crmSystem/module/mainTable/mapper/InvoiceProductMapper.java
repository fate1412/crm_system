package com.fate1412.crmSystem.module.mainTable.mapper;

import com.fate1412.crmSystem.module.mainTable.pojo.InvoiceProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.module.mainTable.pojo.OrderProduct;
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
    
    /**
     * 获取购买数量和已发货数量
     */
    List<OrderProduct> getUnInvoiceNum(@Param("salesOrderId") Long salesOrderId);
    
    /**
     * 获取订单产品
     */
    List<OrderProduct> getOrderProductList(@Param("salesOrderId") Long salesOrderId, @Param("productId") Long productId);
    
    /**
     * 获取该订单下的其他发货单产品(当前发货单除外)
     * @param invoiceIds 要排除的发货单
     */
    List<InvoiceProduct> getInvoiceProductList(@Param("salesOrderId") Long salesOrderId, @Param("invoiceIds") List<Long> invoiceIds);
}
