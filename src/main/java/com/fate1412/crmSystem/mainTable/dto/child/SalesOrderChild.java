package com.fate1412.crmSystem.mainTable.dto.child;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
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
    @TableTitle(value = "产品id",link = true, formType = TableTitle.FormType.Select)
    private Long productId;
    
    private IdToName productIdR =new IdToName(TableNames.product);
    
    /**
     * 购买数量
     */
    @TableField("product_num")
    @TableTitle(value = "购买数量")
    private Integer productNum;
    
    /**
     * 折扣(0-100)
     */
    @TableField("discount")
    @TableTitle(value = "折扣/%")
    private Integer discount;
    
    private final Boolean isEditor = false;
}