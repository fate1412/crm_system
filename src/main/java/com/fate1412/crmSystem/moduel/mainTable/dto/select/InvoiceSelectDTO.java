package com.fate1412.crmSystem.moduel.mainTable.dto.select;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.moduel.mainTable.constant.TableNames;
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
public class InvoiceSelectDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    @TableTitle(value = "发货单Id", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 销售订单id
     */
    @TableField("sales_order_id")
    @TableTitle(value = "销售订单id", link = true, disabled = true, formType = FormType.Select,inserted = true)
    private Long salesOrderId;
    
    private IdToName salesOrderIdR = new IdToName(TableNames.salesOrder);
    
    /**
     * 客户id
     */
    @TableField("customer_id")
    @TableTitle(value = "客户名称", link = true, disabled = true, formType = FormType.Select,inserted = true)
    private Long customerId;
    
    private IdToName customerIdR = new IdToName(TableNames.customer);
    
    /**
     * 计划发货日期
     */
    @TableField("plan_invoice_date")
    @TableTitle(value = "计划发货日期", formType = FormType.Date,inserted = true)
    private Date planInvoiceDate;
    
    /**
     * 发货日期
     */
    @TableField("invoice_date")
    @TableTitle(value = "发货日期", formType = FormType.Date,inserted = true)
    private Date invoiceDate;
    
    /**
     * 是否发货
     */
    @TableField("is_invoice")
    @TableTitle(value = "是否发货",inserted = true)
    private Boolean isInvoice;
    
    /**
     * 物流单号
     */
    @TableField("logistics_id")
    @TableTitle(value = "物流单号",inserted = true)
    private String logisticsId;
    
    /**
     * 收货地址
     */
    @TableField("address")
    @TableTitle(value = "收货地址",inserted = true)
    private String address;
    
    /**
     * 收货日期
     */
    @TableField("receipt_time")
    @TableTitle(value = "收货日期", formType = FormType.Date,inserted = true)
    private Date receiptTime;
    
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    @TableTitle(value = "创建时间", disabled = true, formType = FormType.DateTime)
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    @TableTitle(value = "更新时间", disabled = true, formType = FormType.DateTime)
    private Date updateTime;
    
    /**
     * 创建人
     */
    @TableField("creater")
    @TableTitle(value = "创建人", link = true, disabled = true, formType = FormType.Select)
    private Long creater;
    
    private IdToName createrR = new IdToName(TableNames.sysUser);
    
    /**
     * 修改人
     */
    @TableField("updater")
    @TableTitle(value = "修改人", link = true, disabled = true, formType = FormType.Select)
    private Long updater;
    
    private IdToName updaterR = new IdToName(TableNames.sysUser);
    
    
}
