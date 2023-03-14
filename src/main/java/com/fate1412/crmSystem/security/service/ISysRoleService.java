package com.fate1412.crmSystem.security.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.security.dto.insert.SysRoleInsertDTO;
import com.fate1412.crmSystem.security.dto.select.SysRolePermissionDTO;
import com.fate1412.crmSystem.security.dto.update.SysRoleUpdateDTO;
import com.fate1412.crmSystem.security.dto.update.SysUserUpdateDTO;
import com.fate1412.crmSystem.security.pojo.SysPermission;
import com.fate1412.crmSystem.security.pojo.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.security.pojo.SysRolePermission;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.TableResultData;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-21
 */
public interface ISysRoleService extends IService<SysRole>, MyBaseService<SysRole> {
    
    JsonResult<?> updateByDTO(SysRoleUpdateDTO sysRoleUpdateDTO);
    
    JsonResult<?> updateByEntity(SysRole sysRole);
    
    JsonResult<?> addByDTO(SysRoleInsertDTO sysRoleInsertDTO);
    
    JsonResult<?> addEntity(SysRole sysRole);
    
    TableResultData getColumns();
    
    
    /**
     * 通过Id获取角色的权限
     */
    List<SysPermission> getPermissionById(Long id);
    
    
    /**
     * 通过Id获取角色的权限
     */
    List<SysRolePermissionDTO> getRolePermissionDTOById(Long id);
    
    boolean updatePermissions(Long id, List<SysRolePermissionDTO> list);
    
    List<IdToName> getPermissionsOptions(String nameLike, Integer page);
}
