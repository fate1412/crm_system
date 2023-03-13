package com.fate1412.crmSystem.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.security.dto.select.SysUserSelectDTO;
import com.fate1412.crmSystem.security.dto.update.SysUserUpdateDTO;
import com.fate1412.crmSystem.security.mapper.*;
import com.fate1412.crmSystem.security.pojo.*;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.TableResultData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    
    @Autowired
    private ITableOptionService tableOptionService;
    
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
        List<Long> roleIdList = MyCollections.objects2List(userRoleList, SysUserRole::getRoleId);
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
        List<Long> roleIdList = MyCollections.objects2List(userRoleList, SysUserRole::getRoleId);
        //获取角色对应的权限Id
        QueryWrapper<SysRolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.lambda().in(SysRolePermission::getRoleId,roleIdList);
        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.selectList(rolePermissionQueryWrapper);
        List<Long> permissionIds = MyCollections.objects2List(rolePermissions, SysRolePermission::getPermissionId);
    
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
    public List<?> getDTOList(List<SysUser> sysUserList) {
        return MyCollections.copyListProperties(sysUserList, SysUserSelectDTO::new);
    }
    
    @Override
    public BaseMapper<SysUser> mapper() {
        return sysUserMapper;
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(SysUserUpdateDTO sysUserUpdateDTO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserUpdateDTO,sysUser);
        return updateByEntity(sysUser);
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByEntity(SysUser sysUser) {
        return update(new MyEntity<SysUser>(sysUser) {
            @Override
            public SysUser set(SysUser sysUser) {
                sysUser.setUpdateTime(new Date());
                return sysUser;
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> addByDTO(SysUserUpdateDTO sysUserUpdateDTO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserUpdateDTO,sysUser);
        return addEntity(sysUser);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addEntity(SysUser sysUser) {
        return add(new MyEntity<SysUser>(sysUser) {
            @Override
            public SysUser set(SysUser sysUser) {
                sysUser
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date());
                return sysUser;
            }
        });
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.sysUser, new SysUserSelectDTO(),tableOptionService);
    }
    
    @Override
    public SysUser thisUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return getByUserName(userName);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(SysUser::getUserId, SysUser::getRealName)
                .like(SysUser::getRealName,nameLike);
        IPage<SysUser> iPage = new Page<>(page,10);
        sysUserMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList(iPage.getRecords(), SysUser::getUserId, SysUser::getRealName);
    }
    
    @Override
    public List<IdToName> getRoleOptions(String nameLike, Integer page) {
        
        return null;
    }
}
