package com.fate1412.crmSystem.security.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fate1412.crmSystem.security.dto.SysUserDTO;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.security.pojo.SysUserRole;
import com.fate1412.crmSystem.utils.MyCollections;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author fate1412
 * @since 2023-02-21
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 通过用户名查询
     */
    default SysUser getByUserName(String username) {
        QueryWrapper<SysUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().eq(SysUser::getUsername,username);
        return selectOne(userQueryWrapper);
    }
}
