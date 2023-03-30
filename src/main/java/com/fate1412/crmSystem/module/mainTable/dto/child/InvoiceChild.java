package com.fate1412.crmSystem.module.mainTable.dto.child;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@Accessors(chain = true)
public class InvoiceChild implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableField("id")
    @TableTitle(value = "发货单产品id", disabled = true)
    private Long id;
    
    /**
     * 发货单id
     */
    @TableField("invoice_id")
    private Long invoiceId;
    
    private IdToName invoiceIdR = new IdToName(TableNames.invoice);
    
    /**
     * 产品id
     */
    @TableField("product_id")
    @TableTitle(value = "产品id", link = true, formType = FormType.Select)
    private Long productId;
    
    private IdToName productIdR = new IdToName(TableNames.product);
    
    /**
     * 发货数量
     */
    @TableField("invoice_num")
    @TableTitle(value = "发货数量",inserted = true, formType = FormType.Integer)
    private Integer invoiceNum;
    
    private final Boolean isEditor = false;
}
