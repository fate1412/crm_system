package com.fate1412.crmSystem.module.security.dto.insert;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class SysUserInsertDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    @TableTitle(value = "用户ID",fixed = true,disabled = true)
    private Long userId;
    
    @TableField("username")
    @TableTitle(value = "用户名", disabled = true, inserted = true)
    private String username;
    
    @TableField("real_name")
    @TableTitle(value = "用户姓名", inserted = true)
    private String realName;

    @TableField("phone")
    @TableTitle(value = "手机号" ,inserted = true)
    private String phone;
    
    @TableTitle(value = "是否锁定",formType = FormType.Switch, inserted = true)
    @TableField("lock_flag")
    private Boolean lockFlag;
    
    
}
