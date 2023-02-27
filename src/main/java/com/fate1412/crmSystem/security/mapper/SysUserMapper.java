package com.fate1412.crmSystem.security.mapper;

import com.fate1412.crmSystem.security.pojo.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

    default Map<Long,String> getNameByIds(List<Long> ids) {
        List<SysUser> sysUserList = selectBatchIds(ids);
        return MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
    }
}
