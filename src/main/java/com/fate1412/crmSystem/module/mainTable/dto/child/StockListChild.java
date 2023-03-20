package com.fate1412.crmSystem.module.mainTable.dto.child;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 备货单产品
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stock_list_product")
@Accessors(chain = true)
public class StockListChild implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    @TableTitle(value = "备货单产品Id", disabled = true)
    private Long id;
    
    /**
     * 备货单id
     */
    @TableField("stock_list_id")
    private Long stockListId;
    
    private IdToName stockListIdR = new IdToName(TableNames.stockList);
    
    /**
     * 产品id
     */
    @TableField("product_id")
    @TableTitle(value = "产品", link = true, formType = FormType.Select)
    private Long productId;
    
    private IdToName productIdR = new IdToName(TableNames.product);
    
    /**
     * 备货数量
     */
    @TableField("stock_num")
    @TableTitle(value = "备货数量")
    private Integer stockNum;
    
    /**
     * 价格
     */
    @TableField("price")
    @TableTitle(value = "单价")
    private Double price;
    
    private final Boolean isEditor = false;
}
