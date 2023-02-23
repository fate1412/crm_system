package com.fate1412.crmSystem.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    /**
     * 销售订单id
     */
    @TableField("sales_order_id")
    private Integer salesOrderId;

    /**
     * 计划发货日期
     */
    @TableField("plan_invoice_date")
    private LocalDate planInvoiceDate;

    /**
     * 发货日期
     */
    @TableField("invoice_date")
    private LocalDate invoiceDate;

    /**
     * 是否发货
     */
    @TableField("is_invoice")
    private Integer isInvoice;

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
    private LocalDateTime receiptTime;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private Integer customerId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;

    /**
     * 修改人
     */
    @TableField("updater")
    private String updater;

    @TableField("del_flag")
    @TableLogic
    private String delFlag;


}
