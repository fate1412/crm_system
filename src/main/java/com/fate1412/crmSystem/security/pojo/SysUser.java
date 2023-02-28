package com.fate1412.crmSystem.security.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private Long userId;
    
    @TableField("account")
    private String account;

    @TableField("username")
    private String username;
    
    @TableField("real_name")
    private String realName;

    @TableField("password")
    private String password;

    @TableField("phone")
    private String phone;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;
    
    @TableField("last_login_time")
    private Date lastLoginTime;

    @TableField("lock_flag")
    private Boolean lockFlag;
    
    @TableLogic
    @TableField(value = "del_flag")
    private Boolean delFlag;


}
