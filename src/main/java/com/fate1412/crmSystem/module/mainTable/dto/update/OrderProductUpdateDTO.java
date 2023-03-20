package com.fate1412.crmSystem.module.mainTable.dto.update;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 订单产品
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("order_product")
public class OrderProductUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    private Long id;

    /**
     * 销售订单id
     */
    @TableField("sales_order_id")
    private Long salesOrderId;

    /**
     * 产品id
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 购买数量
     */
    @TableField("product_num")
    private Integer productNum;

    /**
     * 已发货数量
     */
    @TableField("invoice_num")
    private Integer invoiceNum;
    
    /**
     * 折扣(0-100)
     */
    @TableField("discount")
    private Integer discount;



}
