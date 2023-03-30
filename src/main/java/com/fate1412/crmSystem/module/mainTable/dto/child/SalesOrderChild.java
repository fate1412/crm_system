package com.fate1412.crmSystem.module.mainTable.dto.child;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;

@Data
public class SalesOrderChild {
    @TableId("id")
    private Long id;
    
    /**
     * 产品id
     */
    @TableField("product_id")
    @TableTitle(value = "产品",link = true, formType = FormType.Select)
    private Long productId;
    
    private IdToName productIdR =new IdToName(TableNames.product);
    
    
    /**
     * 销售订单id
     */
    @TableField("sales_order_id")
    private Long salesOrderId;
    
    /**
     * 购买数量
     */
    @TableField("product_num")
    @TableTitle(value = "购买数量",formType = FormType.Integer)
    private Integer productNum;
    
    /**
     * 折扣(0-100)
     */
    @TableField("discount")
    @TableTitle(value = "折扣/%",formType = FormType.Integer)
    private Integer discount;
    
    private final Boolean isEditor = false;
}