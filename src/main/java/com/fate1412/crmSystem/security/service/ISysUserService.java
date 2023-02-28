package com.fate1412.crmSystem.security.service;

import com.fate1412.crmSystem.security.dto.SysUserDTO;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.security.pojo.SysUserRole;

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
     * 通过账号查询
     */
    SysUser getByAccount(String account);
    
    /**
     * 通过用户名获取用户所有权限
     */
    List<SysUserRole> getRoleByUserName(String username);
    
    /**
     * 通过账号获取用户所有权限
     */
    List<SysUserRole> getRoleByAccount(String account);
    
    /**
     * 通过id查询
     */
    List<SysUserDTO> getDTOListById(List<Long> ids);

}
