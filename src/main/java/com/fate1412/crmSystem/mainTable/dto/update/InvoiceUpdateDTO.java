package com.fate1412.crmSystem.mainTable.dto.update;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
public class InvoiceUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @TableTitle(value = "发货单Id",fixed = true,disabled = true)
    private Long id;

    /**
     * 计划发货日期
     */
    @TableField("plan_invoice_date")
    @TableTitle(value = "计划发货日期",disabled = true,formType = FormType.Date)
    private Date planInvoiceDate;

    /**
     * 发货日期
     */
    @TableField("invoice_date")
    @TableTitle(value = "发货日期",disabled = true,formType = FormType.Date)
    private Date invoiceDate;

    /**
     * 是否发货
     */
    @TableField("is_invoice")
    @TableTitle("是否发货")
    private Boolean isInvoice;

    /**
     * 物流单号
     */
    @TableField("logistics_id")
    @TableTitle("物流单号")
    private String logisticsId;

    /**
     * 收货地址
     */
    @TableField("address")
    @TableTitle("收货地址")
    private String address;

    /**
     * 收货日期
     */
    @TableField("receipt_time")
    @TableTitle(value = "收货日期",formType = FormType.Date)
    private Date receiptTime;

    

}
