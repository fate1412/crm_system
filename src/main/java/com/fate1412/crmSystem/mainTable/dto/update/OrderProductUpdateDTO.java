package com.fate1412.crmSystem.mainTable.dto.update;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
     * 原总价
     */
    @TableField("original_prices")
    private Double originalPrices;

    /**
     * 折后总价
     */
    @TableField("discount_prices")
    private Double discountPrices;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    private Long creater;

    /**
     * 修改人
     */
    @TableField("updater")
    private Long updater;

    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;


}
