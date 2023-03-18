package com.fate1412.crmSystem.moduel.mainTable.dto.insert;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.moduel.mainTable.constant.TableNames;
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
public class StockListProductInsertDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    @TableTitle(value = "备货单产品Id", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 备货单id
     */
    @TableField("stock_list_id")
    @TableTitle(value = "备货单Id", link = true, disabled = true, formType = FormType.Select)
    private Long stockListId;
    
    private IdToName stockListIdR = new IdToName(TableNames.stockList);
    
    /**
     * 产品id
     */
    @TableField("product_id")
    @TableTitle(value = "产品id", link = true, disabled = true, formType = FormType.Select)
    private Long productId;
    
    private IdToName productIdR = new IdToName(TableNames.product);
    
    /**
     * 备货数量
     */
    @TableField("stock_num")
    @TableTitle("备货数量")
    private Integer stockNum;
    
    /**
     * 价格
     */
    @TableField("price")
    private Double price;
    
    
}
