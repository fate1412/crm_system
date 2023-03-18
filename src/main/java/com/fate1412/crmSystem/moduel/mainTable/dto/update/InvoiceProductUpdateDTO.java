package com.fate1412.crmSystem.moduel.mainTable.dto.update;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class InvoiceProductUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableField("id")
    private Long id;
    
    /**
     * 发货单id
     */
    @TableField("invoice_id")
    private Long invoiceId;
    
    
    /**
     * 产品id
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 发货数量
     */
    @TableField("invoice_num")
    private Integer invoiceNum;
    

}
