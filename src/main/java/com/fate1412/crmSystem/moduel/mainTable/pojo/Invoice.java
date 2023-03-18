package com.fate1412.crmSystem.moduel.mainTable.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 发货单
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("invoice")
@Accessors(chain = true)
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 销售订单id
     */
    @TableField("sales_order_id")
    private Long salesOrderId;

    /**
     * 计划发货日期
     */
    @TableField("plan_invoice_date")
    private Date planInvoiceDate;

    /**
     * 发货日期
     */
    @TableField("invoice_date")
    private Date invoiceDate;

    /**
     * 是否发货
     */
    @TableField("is_invoice")
    private Boolean isInvoice;

    /**
     * 物流单号
     */
    @TableField("logistics_id")
    private String logisticsId;

    /**
     * 收货地址
     */
    @TableField("address")
    private String address;

    /**
     * 收货日期
     */
    @TableField("receipt_time")
    private Date receiptTime;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private Long customerId;

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
