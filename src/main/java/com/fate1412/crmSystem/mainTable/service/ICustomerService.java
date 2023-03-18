package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.mainTable.dto.insert.CustomerInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.CustomerSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.CustomerUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;

/**
 * <p>
 * 客户 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface ICustomerService extends IService<Customer>, MyBaseService<Customer> {
    
    JsonResult<?> updateByDTO(CustomerUpdateDTO customerUpdateDTO);
    
    JsonResult<?> addDTO(CustomerInsertDTO customerInsertDTO);
    
    JsonResult<?> addEntity(Customer customer);
    
    JsonResult<?> updateByEntity(Customer customer);
    
    MyPage listByPage(SelectPage<CustomerSelectDTO> selectPage);
    
}
