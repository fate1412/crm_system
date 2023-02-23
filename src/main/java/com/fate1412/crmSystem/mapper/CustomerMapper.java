package com.fate1412.crmSystem.mapper;

import com.fate1412.crmSystem.pojo.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 客户 Mapper 接口
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}
