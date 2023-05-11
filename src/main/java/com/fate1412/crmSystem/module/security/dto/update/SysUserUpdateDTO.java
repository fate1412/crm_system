package com.fate1412.crmSystem.module.security.dto.update;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
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
public class SysUserUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    @TableTitle(value = "员工ID",fixed = true)
    private Long userId;
    
    @TableField("username")
    @TableTitle(value = "用户名", disabled = true, inserted = true)
    private String username;
    
    @TableField("real_name")
    @TableTitle("员工姓名")
    private String realName;

    @TableField("phone")
    @TableTitle("手机号")
    private String phone;
    
}
