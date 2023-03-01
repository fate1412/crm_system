package com.fate1412.crmSystem.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fate1412.crmSystem.security.dto.SysUserDTO;
import com.fate1412.crmSystem.security.mapper.*;
import com.fate1412.crmSystem.security.pojo.*;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.utils.MyCollections;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-21
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    
    @Autowired
    private SysRoleMapper sysRoleMapper;
    
    @Override
    public SysUser getByUserName(String username) {
        return sysUserMapper.getByUserName(username);
    }
    
    @Override
    public List<SysRole> getRoleById(Long id) {
        //获取用户对应的角色id
        QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.lambda().eq(SysUserRole::getUserId, id);
        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(userRoleQueryWrapper);
        List<Integer> roleIdList = MyCollections.objects2List(userRoleList, SysUserRole::getRoleId);
        //通过角色id获取角色
        QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.lambda().in(SysRole::getRoleId,roleIdList);
        return sysRoleMapper.selectList(roleQueryWrapper);
    }
    
    @Override
    public List<SysRole> getRoleByUserName(String username) {
        SysUser sysUser = sysUserMapper.getByUserName(username);
        if (sysUser == null) {
            return null;
        }
        return getRoleById(sysUser.getUserId());
    }
    
    @Override
    public List<SysPermission> getPermissionById(Long id) {
        //获取用户对应的角色id
        QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.lambda().eq(SysUserRole::getUserId, id);
        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(userRoleQueryWrapper);
        List<Integer> roleIdList = MyCollections.objects2List(userRoleList, SysUserRole::getRoleId);
        //获取角色对应的权限Id
        QueryWrapper<SysRolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.lambda().in(SysRolePermission::getRoleId,roleIdList);
        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.selectList(rolePermissionQueryWrapper);
        List<Integer> permissionIds = MyCollections.objects2List(rolePermissions, SysRolePermission::getPermissionId);
    
        //获取权限
        QueryWrapper<SysPermission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.lambda().in(SysPermission::getPermissionId,permissionIds);
        return sysPermissionMapper.selectList(permissionQueryWrapper);
    }
    
    @Override
    public List<SysPermission> getPermissionByUserName(String username) {
        SysUser sysUser = sysUserMapper.getByUserName(username);
        if (sysUser == null) {
            return null;
        }
        return getPermissionById(sysUser.getUserId());
    }
    
    @Override
    public List<SysUserDTO> getDTOListById(List<Long> ids) {
        List<SysUser> sysUserList = sysUserMapper.selectBatchIds(ids);
        return MyCollections.copyListProperties(sysUserList,SysUserDTO::new);
    }
}
