package com.fate1412.crmSystem.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.security.dto.insert.SysRoleInsertDTO;
import com.fate1412.crmSystem.security.dto.select.SysRolePermissionDTO;
import com.fate1412.crmSystem.security.dto.select.SysRoleSelectDTO;
import com.fate1412.crmSystem.security.dto.update.SysRoleUpdateDTO;
import com.fate1412.crmSystem.security.mapper.SysPermissionMapper;
import com.fate1412.crmSystem.security.pojo.SysPermission;
import com.fate1412.crmSystem.security.pojo.SysRole;
import com.fate1412.crmSystem.security.mapper.SysRoleMapper;
import com.fate1412.crmSystem.security.pojo.SysRolePermission;
import com.fate1412.crmSystem.security.service.ISysRolePermissionService;
import com.fate1412.crmSystem.security.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-21
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private ITableOptionService tableOptionService;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private ISysRolePermissionService rolePermissionService;
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(SysRoleUpdateDTO sysRoleUpdateDTO) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleUpdateDTO,sysRole);
        return updateByEntity(sysRole);
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByEntity(SysRole sysRole) {
        return update(new MyEntity<SysRole>(sysRole) {
            @Override
            public SysRole set(SysRole sysRole) {
                sysRole.setUpdateTime(new Date());
                return sysRole;
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> addByDTO(SysRoleInsertDTO sysRoleInsertDTO) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleInsertDTO,sysRole);
        return addEntity(sysRole);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addEntity(SysRole sysRole) {
        return add(new MyEntity<SysRole>(sysRole) {
            @Override
            public SysRole set(SysRole sysRole) {
                sysRole
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date());
                return sysRole;
            }
        });
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.sysRole, new SysRoleSelectDTO(), tableOptionService);
    }
    
    @Override
    public List<SysPermission> getPermissionById(Long id) {
        QueryWrapper<SysRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRolePermission::getRoleId,id);
        List<SysRolePermission> sysRolePermissions = rolePermissionService.list(queryWrapper);
        List<Long> idList = MyCollections.objects2List(sysRolePermissions, SysRolePermission::getPermissionId);
        if (MyCollections.isEmpty(idList)) {
            return new ArrayList<>();
        }
        return sysPermissionMapper.selectBatchIds(idList);
    }
    
    @Override
    public List<SysRolePermissionDTO> getRolePermissionDTOById(Long id) {
        List<SysPermission> sysPermissionList = getPermissionById(id);
        return MyCollections.copyListProperties(sysPermissionList,SysRolePermissionDTO::new);
    }
    
    @Override
    @Transactional
    public boolean updatePermissions(Long id, List<SysRolePermissionDTO> list) {
        QueryWrapper<SysRolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRolePermission::getRoleId,id);
        rolePermissionService.remove(queryWrapper);
        if (MyCollections.isEmpty(list)) {
            return true;
        }
        List<SysRolePermission> sysRolePermissionList = list.stream().map(dto -> {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setRoleId(id);
            sysRolePermission.setPermissionId(dto.getPermissionId());
            return sysRolePermission;
        }).collect(Collectors.toList());
        return rolePermissionService.saveBatch(sysRolePermissionList);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(SysRole::getRoleId, SysRole::getRoleName)
                .like(SysRole::getRoleName,nameLike);
        IPage<SysRole> iPage = new Page<>(page,10);
        sysRoleMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList(iPage.getRecords(), SysRole::getRoleId, SysRole::getRoleName);
    }
    
    @Override
    public List<IdToName> getPermissionsOptions(String nameLike, Integer page) {
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(SysPermission::getPermissionId, SysPermission::getPermissionCode)
                .like(SysPermission::getPermissionCode,nameLike);
        IPage<SysPermission> iPage = new Page<>(page,10);
        sysPermissionMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList(iPage.getRecords(), SysPermission::getPermissionId, SysPermission::getPermissionCode);
    }
    
    @Override
    public List<?> getDTOList(List<SysRole> sysRoleList) {
        return MyCollections.copyListProperties(sysRoleList, SysRoleSelectDTO::new);
    }
    
    @Override
    public BaseMapper<SysRole> mapper() {
        return sysRoleMapper;
    }
    
    private ResultCode isRight(SysRole sysRole) {
        //角色名称
        if (sysRole.getRoleName() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        if (sysRole.getRoleName().trim().equals("")) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //角色编码
        if (sysRole.getRoleCode() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        if (sysRole.getRoleCode().trim().equals("")) {
            return ResultCode.PARAM_NOT_VALID;
        }
        return ResultCode.SUCCESS;
    }
}
