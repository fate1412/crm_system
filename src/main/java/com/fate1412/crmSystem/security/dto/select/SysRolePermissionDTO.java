package com.fate1412.crmSystem.security.dto.select;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 角色权限表
 * </p>
 *
 * @author fate1412
 * @since 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRolePermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * 权限ID
     */
    @TableId(value = "permission_id", type = IdType.ASSIGN_ID)
    private Long permissionId;
    
    @TableField("permission_code")
    private String permissionCode;
    
    @TableField("permission_desc")
    private String permissionDesc;


}
