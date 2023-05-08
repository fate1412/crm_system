package com.fate1412.crmSystem.module.mainTable.dto.select;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
public class StockListSelectDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    @TableTitle(value = "备货单Id", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 备货日期
     */
    @TableField("stock_up_date")
    @TableTitle(value = "备货日期", formType = FormType.Date, inserted = true)
    private Date stockUpDate;
    
    /**
     * 总价
     */
    @TableField("prices")
    @TableTitle(value = "总价/元", disabled = true, formType = FormType.Double)
    private Double prices;
    
    /**
     * 是否完成备货
     */
    @TableField("is_stock_up")
    @TableTitle(value = "是否完成备货", formType = FormType.Boolean, disabled = true)
    private Boolean isStockUp;
    
    /**
     * 是否完成受理
     */
    @TableField("is_acceptance")
    @TableTitle(value = "是否完成受理", formType = FormType.Boolean)
    private Boolean isAcceptance;
    
    /**
     * 是否紧急
     */
    @TableField("is_pressing")
    @TableTitle(value = "是否紧急", inserted = true, formType = FormType.Boolean, disabled = true)
    private Boolean isPressing;
    
    /**
     * 负责人
     */
    @TableField("owner")
    @TableTitle(value = "负责人", link = true, disabled = true, formType = FormType.Select, inserted = true)
    private Long owner;
    
    private IdToName ownerR = new IdToName(TableNames.sysUser);
    
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
     * 是否通过审批
     */
    @TableField("pass")
    @TableTitle(value = "是否审批", disabled = true)
    private String pass;
    
}
