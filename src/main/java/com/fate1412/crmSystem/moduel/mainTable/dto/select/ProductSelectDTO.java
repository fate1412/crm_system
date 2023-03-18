package com.fate1412.crmSystem.moduel.mainTable.dto.select;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.moduel.mainTable.constant.TableNames;
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
    @TableTitle(value = "产品Id", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 产品名称
     */
    @TableField("name")
    @TableTitle(value = "产品名称",inserted = true)
    private String name;
    
    /**
     * 产品单价
     */
    @TableField("price")
    @TableTitle(value = "产品单价/元",inserted = true)
    private Double price;
    
    /**
     * 产品库存
     */
    @TableField("stock")
    @TableTitle(value = "产品库存/件",inserted = true)
    private Integer stock;
    
    /**
     * 真实库存
     */
    @TableField("real_stock")
    @TableTitle(value = "真实库存/件")
    private Integer realStock;
    
    /**
     * 销售量
     */
    @TableField("sales_volume")
    @TableTitle(value = "销售量/件")
    private Integer salesVolume;
    
    /**
     * 是否上架
     */
    @TableField("is_shelf")
    @TableTitle(value = "是否上架",disabled = true)
    private Boolean isShelf;
    
    /**
     * 上架时间
     */
    @TableField("on_shelf_time")
    @TableTitle(value = "上架时间", formType = FormType.DateTime, inserted = true)
    private Date onShelfTime;
    
    /**
     * 下架时间
     */
    @TableField("off_shelf_time")
    @TableTitle(value = "下架时间", formType = FormType.DateTime, inserted = true)
    private Date offShelfTime;
    
    /**
     * 创建人
     */
    @TableField("creater")
    @TableTitle(value = "创建人", link = true, disabled = true, formType = FormType.Select)
    private Long creater;
    
    private IdToName createrR = new IdToName(TableNames.sysUser);
    
    /**
     * 修改人
     */
    @TableField("updater")
    @TableTitle(value = "修改人", link = true, disabled = true, formType = FormType.Select)
    private Long updater;
    
    private IdToName updaterR = new IdToName(TableNames.sysUser);
    
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    @TableTitle(value = "创建时间", disabled = true, formType = FormType.DateTime)
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    @TableTitle(value = "更新时间", disabled = true, formType = FormType.DateTime)
    private Date updateTime;
    
}
