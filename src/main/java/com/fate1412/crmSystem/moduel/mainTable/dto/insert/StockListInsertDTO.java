package com.fate1412.crmSystem.moduel.mainTable.dto.insert;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.moduel.mainTable.constant.TableNames;
import com.fate1412.crmSystem.moduel.mainTable.dto.child.StockListChild;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 备货单
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stock_list")
public class StockListInsertDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    @TableTitle(value = "备货单Id",fixed = true, disabled = true)
    private Long id;
    
    /**
     * 备货日期
     */
    @TableField("stock_up_date")
    @TableTitle(value = "备货日期", disabled = true, formType = FormType.Date)
    private Date stockUpDate;
    
    /**
     * 总价
     */
    @TableField("prices")
    @TableTitle("总价/元")
    private Double prices;
    
    /**
     * 是否完成备货
     */
    @TableField("is_stock_up")
    @TableTitle("是否完成备货")
    private Boolean isStockUp;
    
    /**
     * 是否完成受理
     */
    @TableField("is_acceptance")
    @TableTitle("是否完成受理")
    private Boolean isAcceptance;
    
    /**
     * 是否紧急
     */
    @TableField("is_pressing")
    @TableTitle("是否紧急")
    private Boolean isPressing;
    
    /**
     * 负责人
     */
    @TableField("owner")
    @TableTitle(value = "负责人", link = true, disabled = true, formType = FormType.Select)
    private Long owner;
    
    private IdToName ownerR = new IdToName(TableNames.sysUser);
    
    private List<StockListChild> childList;

    
}
