package com.fate1412.crmSystem.mainTable.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class Product implements Serializable {

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

    /**
     * 是否上架
     */
    @TableField("is_shelf")
    private Boolean isShelf;

    /**
     * 上架时间
     */
    @TableField("on_shelives_time")
    private Date onShelivesTime;

    /**
     * 下架时间
     */
    @TableField("off_shelives_time")
    private Date offShelivesTime;

    /**
     * 折扣(0-100)
     */
    @TableField("discount")
    private Integer discount;

    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;


}