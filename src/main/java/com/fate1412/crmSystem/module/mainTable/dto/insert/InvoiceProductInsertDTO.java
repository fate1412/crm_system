package com.fate1412.crmSystem.module.mainTable.dto.insert;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 发货单产品
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("invoice_product")
public class InvoiceProductInsertDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableField("id")
    @TableTitle(value = "发货单产品id", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 发货单id
     */
    @TableField("invoice_id")
    @TableTitle(value = "发货单id", link = true, disabled = true, formType = FormType.Select)
    private Long invoiceId;
    
    private IdToName invoiceIdR = new IdToName(TableNames.invoice);
    
    /**
     * 产品id
     */
    @TableField("product_id")
    @TableTitle(value = "产品id", link = true, disabled = true, formType = FormType.Select)
    private Long productId;
    
    private IdToName productIdR = new IdToName(TableNames.product);
    
    /**
     * 发货数量
     */
    @TableField("invoice_num")
    @TableTitle("发货数量")
    private Integer invoiceNum;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    @TableTitle(value = "创建时间", formType = FormType.DateTime, disabled = true)
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    @TableTitle(value = "更新时间", formType = FormType.DateTime, disabled = true)
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
