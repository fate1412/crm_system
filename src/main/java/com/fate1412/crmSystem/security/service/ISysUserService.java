package com.fate1412.crmSystem.security.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.security.dto.insert.SysUserInsertDTO;
import com.fate1412.crmSystem.security.dto.select.SysRolePermissionDTO;
import com.fate1412.crmSystem.security.dto.select.SysUserRolesDTO;
import com.fate1412.crmSystem.security.dto.update.SysUserUpdateDTO;
import com.fate1412.crmSystem.security.pojo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.TableResultData;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-21
 */
public interface ISysUserService extends IService<SysUser>, MyBaseService<SysUser> {
    
    /**
     * 通过用户名查询
     */
    SysUser getByUserName(String username);

    /**
     * 通过id获取用户所有角色
     */
    List<SysRole> getRoleById(Long id);
    
    
    /**
     * 通过id获取用户所有角色
     */
    List<SysUserRolesDTO> getUserRolesById(Long id);
    
    /**
     * 通过用户名获取用户所有角色
     */
    List<SysRole> getRoleByUserName(String username);

    /**
     * 通过Id获取用户的权限
     */
    List<SysPermission> getPermissionById(Long id);
    
    boolean updateRoles(Long id, List<SysUserRolesDTO> userRolesList);
    
    /**
     * 通过用户名获取用户的权限
     */
    List<SysPermission> getPermissionByUserName(String username);
    
    JsonResult<?> updateByDTO(SysUserUpdateDTO sysUserUpdateDTO);
    
    JsonResult<?> updateByEntity(SysUser sysUser);
    
    JsonResult<?> addByDTO(SysUserInsertDTO sysUserInsertDTO);
    
    JsonResult<?> addEntity(SysUser sysUser);
    
    TableResultData getColumns();
    
    SysUser thisUser();
    
    boolean removeUser(Long id);
    
    List<SysRolePermissionDTO> getThisUserPermissions();

}
