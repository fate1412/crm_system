package com.fate1412.crmSystem.module.mainTable.dto.insert;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.mainTable.dto.child.InvoiceChild;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class InvoiceInsertDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    @TableTitle(value = "发货单Id", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 销售订单id
     */
    @TableField("sales_order_id")
    @TableTitle(value = "销售订单id", link = true, disabled = true, formType = FormType.Select)
    private Long salesOrderId;
    
    private IdToName salesOrderIdR = new IdToName(TableNames.salesOrder);
    
    /**
     * 计划发货日期
     */
    @TableField("plan_invoice_date")
    @TableTitle(value = "计划发货日期", formType = FormType.Date)
    private Date planInvoiceDate;
    
    /**
     * 发货日期
     */
    @TableField("invoice_date")
    @TableTitle(value = "发货日期", formType = FormType.Date)
    private Date invoiceDate;
    
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
    @TableTitle(value = "收货日期", formType = FormType.Date)
    private Date receiptTime;

    private List<InvoiceChild> childList;
    
}
