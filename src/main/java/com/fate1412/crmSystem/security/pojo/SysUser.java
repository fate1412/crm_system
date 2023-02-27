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
    @TableId(value = "user_id", type = IdType.AUTO)
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
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;

    @TableField("lock_flag")
    private String lockFlag;
    
    @TableLogic
    @TableField(value = "del_flag")
    private String delFlag;
    
    public boolean isDel() {
        return !delFlag.equals("0");
    }
    
    public boolean isLock() {
        return !lockFlag.equals("0");
    }


}
