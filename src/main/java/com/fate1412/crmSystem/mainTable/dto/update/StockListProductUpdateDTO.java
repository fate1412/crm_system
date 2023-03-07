package com.fate1412.crmSystem.mainTable.dto.update;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
public class StockListProductUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    private Long id;

    /**
     * 备货单id
     */
    @TableField("stock_list_id")
    private Long stockListId;

    /**
     * 产品id
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 备货数量
     */
    @TableField("stock_num")
    private Integer stockNum;



}
