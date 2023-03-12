package com.fate1412.crmSystem.mainTable.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class StockListProduct implements Serializable {

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

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    private Long creater;

    /**
     * 修改人
     */
    @TableField("updater")
    private Long updater;


}
