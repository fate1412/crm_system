package com.fate1412.crmSystem.mainTable.dto.select;

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
 * 销售订单
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalesOrderSelectDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableTitle(value = "销售订单Id", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 原价
     */
    @TableTitle(value = "原价/元", inserted = true)
    private Double originalPrice;
    
    /**
     * 折后价格
     */
    @TableTitle(value = "折后价格/元", inserted = true)
    private Double discountPrice;
    
    /**
     * 是否通过
     */
    @TableTitle(value = "是否通过", inserted = true)
    private Boolean isPass;
    
    /**
     * 客户id
     */
    private IdToName customerIdR = new IdToName(TableNames.customer);
    
    @TableTitle(value = "客户id", link = true, disabled = true, formType = FormType.Select,inserted = true)
    private Long customerId;
    
    /**
     * 发货状态
     */
    @TableTitle(value = "发货状态",inserted = true, formType = FormType.Select)
    private Integer invoiceStatus;
    
    /**
     * 创建时间
     */
    @TableTitle(value = "创建时间", disabled = true, formType = FormType.DateTime)
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableTitle(value = "更新时间", disabled = true, formType = FormType.DateTime)
    private Date updateTime;
    
    /**
     * 创建人
     */
    private IdToName createrR = new IdToName(TableNames.sysUser);
    
    @TableTitle(value = "创建人", link = true, disabled = true, formType = FormType.Select)
    private Long creater;
    
    /**
     * 修改人
     */
    private IdToName updaterR = new IdToName(TableNames.sysUser);
    
    @TableTitle(value = "修改人", link = true, disabled = true, formType = FormType.Date)
    private Long updater;
    
    
}
