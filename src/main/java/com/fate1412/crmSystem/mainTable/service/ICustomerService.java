package com.fate1412.crmSystem.mainTable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate1412.crmSystem.mainTable.dto.CustomerDTO;
import com.fate1412.crmSystem.mainTable.pojo.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 客户 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface ICustomerService extends IService<Customer> {

    IPage<CustomerDTO> listByPage(long thisPage, long pageSize);
    
    List<CustomerDTO> getDTOListById(List<Long> ids);
    
    boolean updateById(CustomerDTO customerDTO);
}
