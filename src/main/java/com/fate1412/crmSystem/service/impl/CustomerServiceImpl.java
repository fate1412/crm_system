package com.fate1412.crmSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.dto.CustomerDTO;
import com.fate1412.crmSystem.pojo.Customer;
import com.fate1412.crmSystem.mapper.CustomerMapper;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultTool;
import com.fate1412.crmSystem.utils.TableResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private CustomerMapper customerMapper;
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Override
    public IPage<CustomerDTO> listByPage(long thisPage, long pageSize) {
        IPage<Customer> page = new Page<>(thisPage,pageSize);
        customerMapper.selectPage(page,null);
        List<Customer> customerList = page.getRecords();
        List<Integer> createIds = MyCollections.objects2List(customerList, Customer::getCreater);
        List<Integer> updateMemberIds = MyCollections.objects2List(customerList, Customer::getUpdateMember);
        List<Integer> ownerIds = MyCollections.objects2List(customerList, Customer::getOwner);
        List<Integer> userIdList = MyCollections.addList(true, createIds, updateMemberIds, ownerIds);
    
        List<SysUser> sysUserList = sysUserMapper.selectBatchIds(userIdList);
        
        Map<Integer, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
    
        List<CustomerDTO> customerDTOList = MyCollections.copyListProperties(customerList, CustomerDTO::new);
        customerDTOList.forEach(customerDTO -> {
            Integer createId = customerDTO.getCreater();
            Integer updateMemberId = customerDTO.getUpdateMember();
            Integer ownerId = customerDTO.getOwner();
            customerDTO.setCreaterR(userMap.get(createId));
            customerDTO.setUpdateMemberR(userMap.get(updateMemberId));
            customerDTO.setOwnerR(userMap.get(ownerId));
        });
        
        IPage<CustomerDTO> iPage = new Page<>(thisPage,pageSize);
        iPage.setCurrent(page.getCurrent());
        iPage.setRecords(customerDTOList);
        return iPage;
    }
}
