package com.fate1412.crmSystem.module.mainTable.dto.select;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 客户
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerSelectDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    @TableTitle(value = "客户ID", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 客户名称
     */
    @TableField("name")
    @TableTitle(value = "客户名称", inserted = true)
    private String name;
    
    /**
     * 客户类型
     */
    @TableField("customer_type")
    @TableTitle(value = "客户类型", formType = FormType.Select, inserted = true)
    private Integer customerType;
    
    private String customerTypeR;
    
    /**
     * 手机号
     */
    @TableField("mobile")
    @TableTitle(value = "手机号", inserted = true)
    private String mobile;
    
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
     * 更新者
     */
    @TableField("update_member")
    @TableTitle(value = "修改人", link = true, disabled = true, formType = FormType.Select)
    private Long updateMember;
    
    private IdToName updateMemberR = new IdToName(TableNames.sysUser);
    
    /**
     * 负责人
     */
    @TableField("owner")
    @TableTitle(value = "负责人", link = true, formType = FormType.Select, inserted = true)
    private Long owner;
    
    private IdToName ownerR = new IdToName(TableNames.sysUser);
    
    /**
     * 是否通过审批
     */
    @TableField("pass")
    @TableTitle(value = "是否审批", disabled = true)
    private String pass;
    
    
}
