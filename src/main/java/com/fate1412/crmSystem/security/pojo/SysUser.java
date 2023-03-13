package com.fate1412.crmSystem.security.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
@Accessors(chain = true)
public class SysUser implements UserDetails,Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;
    
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
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    
    /**
     * 是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    /**
     * 是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return !lockFlag;
    }
    
    /**
     * 是否凭证未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    /**
     * 是否启用
     */
    @Override
    public boolean isEnabled() {
        return !delFlag;
    }
}
