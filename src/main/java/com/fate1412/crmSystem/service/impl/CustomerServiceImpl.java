package com.fate1412.crmSystem.service.impl;

import com.fate1412.crmSystem.pojo.Customer;
import com.fate1412.crmSystem.mapper.CustomerMapper;
import com.fate1412.crmSystem.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
