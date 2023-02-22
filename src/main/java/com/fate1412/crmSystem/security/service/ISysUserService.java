package com.fate1412.crmSystem.security.service;

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
    
    SysUser getByUserName(String username);
    
    List<SysUserRole> getUserRole(String username);

}
