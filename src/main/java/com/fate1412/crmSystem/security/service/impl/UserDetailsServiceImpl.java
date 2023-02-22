package com.fate1412.crmSystem.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fate1412.crmSystem.security.pojo.SysPermission;
import com.fate1412.crmSystem.security.pojo.SysRolePermission;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.pojo.SysUserRole;
import com.fate1412.crmSystem.security.service.*;
import com.fate1412.crmSystem.utils.MyCollections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private ISysPermissionService permissionService;
    
    @Autowired
    private ISysRoleService roleService;
    
    @Autowired
    private ISysUserRoleService userRoleService;
    
    @Autowired
    private ISysRolePermissionService rolePermissionService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        //获取用户
        QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(SysUser::getUsername,username);
        SysUser sysUser = userService.getOne(userQueryWrapper);
        if (sysUser == null) {
            throw new InternalAuthenticationServiceException("账号不存在");
        }
        
        //获取用户所有的角色Id
        QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.lambda().eq(SysUserRole::getUserId, sysUser.getUserId());
        List<SysUserRole> sysUserRoles = userRoleService.list(userRoleQueryWrapper);
        List<Integer> roleIdList = MyCollections.objects2List(sysUserRoles, SysUserRole::getRoleId);
        
        //获取角色对应的权限Id
        QueryWrapper<SysRolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.lambda().in(SysRolePermission::getRoleId,roleIdList);
        List<SysRolePermission> rolePermissions = rolePermissionService.list(rolePermissionQueryWrapper);
        List<Integer> permissionIds = MyCollections.objects2List(rolePermissions, SysRolePermission::getPermissionId);
        
        //获取权限
        QueryWrapper<SysPermission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.lambda().in(SysPermission::getPermissionId,permissionIds);
        List<SysPermission> permissions = permissionService.list(permissionQueryWrapper);
        List<String> permissionCodes = MyCollections.objects2List(permissions, SysPermission::getPermissionCode);
    
        //声明用户授权
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        permissionCodes.forEach(permissionCode -> {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permissionCode);
            grantedAuthorities.add(grantedAuthority);
        });
        log.info(sysUser.isLock()+"");
        return new User(sysUser.getAccount(),sysUser.getPassword(),!sysUser.isDel(),true,true,!sysUser.isLock(),grantedAuthorities);
    }
}
