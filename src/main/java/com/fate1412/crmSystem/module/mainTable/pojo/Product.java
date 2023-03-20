package com.fate1412.crmSystem.module.mainTable.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
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
    @TableField("on_shelf_time")
    private Date onShelfTime;

    /**
     * 下架时间
     */
    @TableField("off_shelf_time")
    private Date offShelfTime;

    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;

    public boolean isShelf() {
        Date date = new Date();
        return date.after(onShelfTime) && date.before(offShelfTime);
    }
    
    public Product setIsShelf() {
        isShelf = isShelf();
        return this;
    }
}
