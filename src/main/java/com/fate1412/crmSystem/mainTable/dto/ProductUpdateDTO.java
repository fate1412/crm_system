package com.fate1412.crmSystem.mainTable.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 产品
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("product")
public class ProductUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 产品名称
     */
    @TableField("name")
    private String name;

    /**
     * 产品单价
     */
    @TableField("price")
    private Double price;

    /**
     * 产品库存
     */
    @TableField("stock")
    private Integer stock;

    /**
     * 真实库存
     */
    @TableField("real_stock")
    private Integer realStock;

    /**
     * 销售量
     */
    @TableField("sales_volume")
    private Integer salesVolume;
    /**
     * 是否上架
     */
    @TableField("is_shelf")
    private Boolean isShelf;

    /**
     * 上架时间
     */
    @TableField("on_shelf_time")
    private Date onShelfTime;

    /**
     * 下架时间
     */
    @TableField("off_shelf_time")
    private Date offShelfTime;

    /**
     * 折扣(0-100)
     */
    @TableField("discount")
    private Integer discount;


}
