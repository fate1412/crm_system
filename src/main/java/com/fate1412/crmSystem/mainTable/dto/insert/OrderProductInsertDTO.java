package com.fate1412.crmSystem.mainTable.dto.insert;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.IdToName;
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
public class OrderProductInsertDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    @TableTitle(value = "订单产品id",fixed = true,disabled = true)
    private Long id;

    /**
     * 销售订单id
     */
    @TableField("sales_order_id")
    @TableTitle(value = "销售订单id",link = true,disabled = true, formType = FormType.Select)
    private Long salesOrderId;
    
    private IdToName salesOrderIdR =new IdToName(TableNames.salesOrder);

    /**
     * 产品id
     */
    @TableField("product_id")
    @TableTitle(value = "产品id",link = true,disabled = true, formType = FormType.Select)
    private Long productId;
    
    private IdToName productIdR =new IdToName(TableNames.product);
    
    /**
     * 购买数量
     */
    @TableField("product_num")
    @TableTitle("购买数量")
    private Integer productNum;

    /**
     * 已发货数量
     */
    @TableField("invoice_num")
    @TableTitle("已发货数量")
    private Integer invoiceNum;

    /**
     * 折扣(0-100)
     */
    @TableField("discount")
    @TableTitle("折扣/%")
    private Integer discount;


}
