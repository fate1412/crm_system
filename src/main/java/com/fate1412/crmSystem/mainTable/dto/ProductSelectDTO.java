package com.fate1412.crmSystem.mainTable.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.utils.IdToName;
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
public class ProductSelectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @TableTitle(value = "产品Id",fixed = true,disabled = true)
    private Long id;

    /**
     * 产品名称
     */
    @TableField("name")
    @TableTitle("产品名称")
    private String name;

    /**
     * 产品单价
     */
    @TableField("price")
    @TableTitle("产品单价/元")
    private Double price;

    /**
     * 产品库存
     */
    @TableField("stock")
    @TableTitle("产品库存/件")
    private Integer stock;

    /**
     * 真实库存
     */
    @TableField("real_stock")
    @TableTitle("真实库存/件")
    private Integer realStock;

    /**
     * 销售量
     */
    @TableField("sales_volume")
    @TableTitle("销售量/件")
    private Integer salesVolume;

    /**
     * 是否上架
     */
    @TableField("is_shelf")
    @TableTitle("是否上架")
    private Boolean isShelf;

    /**
     * 上架时间
     */
    @TableField("on_shelf_time")
    @TableTitle(value = "上架时间",disabled = true,formType = TableTitle.FormType.DateTime)
    private Date onShelfTime;

    /**
     * 下架时间
     */
    @TableField("off_shelf_time")
    @TableTitle(value = "下架时间",disabled = true,formType = TableTitle.FormType.DateTime)
    private Date offShelfTime;

    /**
     * 折扣(0-100)
     */
    @TableField("discount")
    @TableTitle("折扣/%")
    private Integer discount;
    
    
    /**
     * 创建人
     */
    @TableField("creater")
    private Long creater;
    
    @TableTitle(value = "创建人",link = true,disabled = true)
    private IdToName createrR;
    
    /**
     * 修改人
     */
    @TableField("updater")
    private Long updater;
    @TableTitle(value = "修改人",link = true,disabled = true)
    private IdToName updaterR;
    
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    @TableTitle(value = "创建时间",disabled = true,formType = TableTitle.FormType.DateTime)
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    @TableTitle(value = "更新时间",disabled = true,formType = TableTitle.FormType.DateTime)
    private Date updateTime;

}
