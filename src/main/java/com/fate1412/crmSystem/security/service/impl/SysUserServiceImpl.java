package com.fate1412.crmSystem.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fate1412.crmSystem.security.dto.SysUserDTO;
import com.fate1412.crmSystem.security.mapper.SysUserRoleMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUserRole;
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
    
    @Override
    public SysUser getByUserName(String username) {
        QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(SysUser::getUsername,username);
        return sysUserMapper.selectOne(userQueryWrapper);
    }
    
    @Override
    public SysUser getByAccount(String account) {
        QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(SysUser::getAccount,account);
        return sysUserMapper.selectOne(userQueryWrapper);
    }
    
    @Override
    public List<SysUserRole> getRoleByUserName(String username) {
        SysUser sysUser = getByUserName(username);
        if (sysUser == null) {
            return null;
        }
        QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.lambda().eq(SysUserRole::getUserId, sysUser.getUserId());
        return sysUserRoleMapper.selectList(userRoleQueryWrapper);
    }
    
    @Override
    public List<SysUserRole> getRoleByAccount(String account) {
        return getRoleByUserName(getByAccount(account).getUsername());
    }
    
    @Override
    public List<SysUserDTO> getDTOListById(List<Long> ids) {
        List<SysUser> sysUserList = sysUserMapper.selectBatchIds(ids);
        return MyCollections.copyListProperties(sysUserList,SysUserDTO::new);
    }
}
