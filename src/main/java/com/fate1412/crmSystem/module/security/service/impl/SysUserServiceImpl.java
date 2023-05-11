package com.fate1412.crmSystem.module.security.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.exception.DataCheckingException;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.security.dto.insert.SysUserInsertDTO;
import com.fate1412.crmSystem.module.security.dto.select.SysRolePermissionDTO;
import com.fate1412.crmSystem.module.security.dto.select.SysUserRolesDTO;
import com.fate1412.crmSystem.module.security.dto.select.SysUserSelectDTO;
import com.fate1412.crmSystem.module.security.dto.update.SysUserUpdateDTO;
import com.fate1412.crmSystem.module.security.mapper.*;
import com.fate1412.crmSystem.module.security.pojo.*;
import com.fate1412.crmSystem.module.security.service.ISysUserRoleService;
import com.fate1412.crmSystem.module.security.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private ISysUserRoleService sysUserRoleService;
    
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
        List<SysUserRole> userRoleList = sysUserRoleService.list(userRoleQueryWrapper);
        List<Long> roleIdList = MyCollections.objects2List(userRoleList, SysUserRole::getRoleId);
        if (MyCollections.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }
        //通过角色id获取角色
        QueryWrapper<SysRole> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.lambda().in(SysRole::getRoleId, roleIdList);
        return sysRoleMapper.selectList(roleQueryWrapper);
    }
    
    @Override
    public List<SysUserRolesDTO> getUserRolesById(Long id) {
        List<SysRole> sysRoles = getRoleById(id);
        return MyCollections.copyListProperties(sysRoles, SysUserRolesDTO::new);
    }
    
    @Override
    public List<SysRole> getRoleByUserName(String username) {
        SysUser sysUser = sysUserMapper.getByUserName(username);
        if (sysUser == null) {
            return new ArrayList<>();
        }
        return getRoleById(sysUser.getUserId());
    }
    
    @Override
    public List<SysPermission> getPermissionById(Long id) {
        //获取用户对应的角色id
        QueryWrapper<SysUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.lambda().eq(SysUserRole::getUserId, id);
        List<SysUserRole> userRoleList = sysUserRoleService.list(userRoleQueryWrapper);
        List<Long> roleIdList = MyCollections.objects2List(userRoleList, SysUserRole::getRoleId);
        if (MyCollections.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }
        //获取角色对应的权限Id
        QueryWrapper<SysRolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.lambda().in(SysRolePermission::getRoleId, roleIdList);
        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.selectList(rolePermissionQueryWrapper);
        List<Long> permissionIds = MyCollections.objects2List(rolePermissions, SysRolePermission::getPermissionId);
        if (MyCollections.isEmpty(permissionIds)) {
            return new ArrayList<>();
        }
        //获取权限
        QueryWrapper<SysPermission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.lambda().in(SysPermission::getPermissionId, permissionIds);
        return sysPermissionMapper.selectList(permissionQueryWrapper);
    }
    
    @Override
    @Transactional
    public boolean updateRoles(Long id, List<SysUserRolesDTO> userRolesList) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserRole::getUserId, id);
        sysUserRoleService.remove(queryWrapper);
        if (MyCollections.isEmpty(userRolesList)) {
            return true;
        }
        List<SysUserRole> list = userRolesList.stream().map(u -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(id);
            sysUserRole.setRoleId(u.getRoleId());
            return sysUserRole;
        }).collect(Collectors.toList());
        return sysUserRoleService.saveBatch(list);
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
        BeanUtils.copyProperties(sysUserUpdateDTO, sysUser);
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
            
            @Override
            public ResultCode verification(SysUser sysUser) {
                return isRight(sysUser);
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> addByDTO(SysUserInsertDTO sysUserInsertDTO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserInsertDTO, sysUser);
        return addEntity(sysUser);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addEntity(SysUser sysUser) {
        return add(new MyEntity<SysUser>(sysUser) {
            @Override
            public SysUser set(SysUser sysUser) {
                sysUser
                        .setPassword(new BCryptPasswordEncoder().encode("123456"))
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date());
                return sysUser;
            }
            
            @Override
            public ResultCode verification(SysUser sysUser) {
                return isRight(sysUser);
            }
        });
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.sysUser, new SysUserSelectDTO(), tableOptionService);
    }
    
    @Override
    public SysUser thisUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        //匿名用户返回null
        if ("anonymousUser".equals(userName)) {
            return null;
        }
        return getByUserName(userName);
    }
    
    @Override
    @Transactional
    public boolean removeUser(Long id) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserRole::getUserId, id);
        sysUserRoleService.remove(queryWrapper);
        return removeById(id);
    }
    
    @Override
    public JSONObject getThisUserPermissions() {
        JSONObject jsonObject = new JSONObject();
        SysUser sysUser = thisUser();
        if(sysUser == null) {
            return jsonObject;
        }
        List<SysPermission> permissionList = getPermissionById(sysUser.getUserId());
        List<SysRolePermissionDTO> rolePermissionDTOS = MyCollections.copyListProperties(permissionList, SysRolePermissionDTO::new);
        jsonObject.put("permissions",rolePermissionDTOS);
        jsonObject.put("name",sysUser.getRealName());
        return jsonObject;
    }
    
    @Override
    public boolean passwdChange(Long userId, String passwd) {
        //密码(不小于6位)
        if (StringUtils.isBlank(passwd) && passwd.trim().length() < 6) {
            throw new DataCheckingException(ResultCode.PARAM_NOT_VALID);
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setPassword(new BCryptPasswordEncoder().encode(passwd.trim()));
        return updateById(sysUser);
    }
    
    @Override
    public MyPage listByPage(SelectPage<SysUserSelectDTO> selectPage) {
        SysUserSelectDTO like = selectPage.getLike();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(like.getUserId() != null, SysUser::getUserId, like.getUserId())
                .like(like.getUsername() != null, SysUser::getUsername, like.getUsername())
                .like(like.getRealName() != null, SysUser::getRealName, like.getRealName());
        return listByPage(selectPage.getPage(), selectPage.getPageSize(), queryWrapper);
    }
    
    @Override
    public boolean lock(Long userId) {
        SysUser sysUser = getById(userId);
        sysUser.setLockFlag(!sysUser.getLockFlag());
        return updateById(sysUser);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(SysUser::getUserId, SysUser::getRealName)
                .like(SysUser::getRealName, nameLike);
        IPage<SysUser> iPage = new Page<>(page, 10);
        sysUserMapper.selectPage(iPage, queryWrapper);
        return IdToName.createList(iPage.getRecords(), SysUser::getUserId, SysUser::getRealName);
    }
    
    private ResultCode isRight(SysUser sysUser) {
        //用户名
        if (StringUtils.isBlank(sysUser.getUsername())) {
            return ResultCode.PARAM_IS_BLANK;
        }
        sysUser.setUsername(sysUser.getUsername().trim());
        //真实姓名
        if (StringUtils.isBlank(sysUser.getRealName())) {
            return ResultCode.PARAM_IS_BLANK;
        }
        sysUser.setRealName(sysUser.getRealName().trim());
        //手机号
        if (sysUser.getPhone() != null && !Pattern.matches("\\d{11}", sysUser.getPhone().trim())) {
            return ResultCode.PARAM_NOT_VALID;
        }
        sysUser.setPhone(sysUser.getPhone().trim());
        return ResultCode.SUCCESS;
    }
    
}
