package com.fate1412.crmSystem.mainTable.dto.insert;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
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
public class ProductInsertDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
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
    @TableTitle("折扣/%")
    private Integer discount;
    
    
    public boolean isShelf() {
        Date date = new Date();
        return date.after(onShelfTime) && date.before(offShelfTime);
    }
    
}
