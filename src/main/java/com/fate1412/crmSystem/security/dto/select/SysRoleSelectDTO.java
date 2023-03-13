package com.fate1412.crmSystem.security.dto.select;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author fate1412
 * @since 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
public class SysRoleSelectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    @TableTitle(value = "角色Id",link = true, fixed = true, disabled = true)
    private Long roleId;

    @TableField("role_name")
    @TableTitle(value = "角色名称",inserted = true)
    private String roleName;

    @TableField("role_code")
    @TableTitle(value = "角色Code",inserted = true)
    private String roleCode;

    @TableField("role_desc")
    @TableTitle(value = "描述",inserted = true)
    private String roleDesc;

    @TableField("create_time")
    @TableTitle(value = "创建时间",disabled = true, formType = FormType.DateTime)
    private Date createTime;

    @TableField("update_time")
    @TableTitle(value = "更新时间",disabled = true, formType = FormType.DateTime)
    private Date updateTime;

    @TableField("del_flag")
    private Boolean delFlag;


}
