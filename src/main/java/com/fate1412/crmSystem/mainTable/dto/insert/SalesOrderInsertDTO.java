package com.fate1412.crmSystem.mainTable.dto.insert;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.child.SalesOrderChild;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class SalesOrderInsertDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableTitle(value = "销售订单Id", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 原价
     */
    @TableTitle("原价/元")
    private Double originalPrice;
    
    /**
     * 折后价格
     */
    @TableTitle("折后价格/元")
    private Double discountPrice;
    
    /**
     * 是否通过
     */
    @TableTitle("是否通过")
    private Boolean isPass;
    
    /**
     * 客户id
     */
    private IdToName customerIdR = new IdToName(TableNames.customer);
    
    @TableTitle(value = "客户id", link = true, disabled = true, formType = FormType.Select)
    private Long customerId;
    
    /**
     * 发货状态
     */
    @TableTitle("发货状态")
    private Integer invoiceStatus;
    
    /**
     * 创建时间
     */
    @TableTitle(value = "创建时间", disabled = true, fixed = true)
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableTitle(value = "更新时间", disabled = true, fixed = true)
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
    
    private List<SalesOrderChild> childList;
    
    
}
