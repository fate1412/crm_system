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
public class SysUserRolesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    private Long roleId;

    @TableField("role_name")
    private String roleName;
}
