package com.fate1412.crmSystem.security.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fate1412.crmSystem.annotations.TableTitle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author fate1412
 * @since 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    @TableTitle(value = "员工ID",fixed = true)
    private Long userId;
    
    @TableField("real_name")
    @TableTitle("员工姓名")
    private String realName;

    @TableField("phone")
    @TableTitle("手机号")
    private String phone;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @TableTitle(value = "创建时间",formType = TableTitle.FormType.Date)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    @TableTitle(value = "修改时间",formType = TableTitle.FormType.Date)
    private Date updateTime;
    
    @TableField("last_login_time")
    @TableTitle(value = "最后登录时间",formType = TableTitle.FormType.Date)
    private Date lastLoginTime;
    
}
