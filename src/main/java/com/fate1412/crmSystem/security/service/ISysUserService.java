package com.fate1412.crmSystem.security.service;

import com.fate1412.crmSystem.security.dto.SysUserDTO;
import com.fate1412.crmSystem.security.pojo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-21
 */
public interface ISysUserService extends IService<SysUser> {
    
    /**
     * 通过用户名查询
     */
    SysUser getByUserName(String username);

    /**
     * 通过id获取用户所有角色
     */
    List<SysRole> getRoleById(Long id);
    
    /**
     * 通过用户名获取用户所有角色
     */
    List<SysRole> getRoleByUserName(String username);

    /**
     * 通过Id获取用户的权限
     */
    List<SysPermission> getPermissionById(Long id);
    
    /**
     * 通过用户名获取用户的权限
     */
    List<SysPermission> getPermissionByUserName(String username);
    
    /**
     * 通过id查询
     */
    List<SysUserDTO> getDTOListById(List<Long> ids);

}
