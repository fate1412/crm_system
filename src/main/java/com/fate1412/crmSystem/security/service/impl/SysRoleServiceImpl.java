package com.fate1412.crmSystem.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.security.dto.insert.SysRoleInsertDTO;
import com.fate1412.crmSystem.security.dto.select.SysRoleSelectDTO;
import com.fate1412.crmSystem.security.dto.select.SysUserSelectDTO;
import com.fate1412.crmSystem.security.dto.update.SysRoleUpdateDTO;
import com.fate1412.crmSystem.security.dto.update.SysUserUpdateDTO;
import com.fate1412.crmSystem.security.pojo.SysRole;
import com.fate1412.crmSystem.security.mapper.SysRoleMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.TableResultData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    
    @Override
    public JsonResult<?> updateByDTO(SysRoleUpdateDTO sysRoleUpdateDTO) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleUpdateDTO,sysRole);
        return updateByEntity(sysRole);
    }
    
    @Override
    public JsonResult<?> updateByEntity(SysRole sysRole) {
        return null;
    }
    
    @Override
    public JsonResult<?> addByDTO(SysRoleInsertDTO sysRoleInsertDTO) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(sysRoleInsertDTO,sysRole);
        return addEntity(sysRole);
    }
    
    @Override
    public JsonResult<?> addEntity(SysRole sysRole) {
        return null;
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.sysRole, new SysRoleSelectDTO(), tableOptionService);
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
    public List<?> getDTOList(List<SysRole> sysRoleList) {
        return MyCollections.copyListProperties(sysRoleList, SysRoleSelectDTO::new);
    }
    
    @Override
    public BaseMapper<SysRole> mapper() {
        return sysRoleMapper;
    }
}
