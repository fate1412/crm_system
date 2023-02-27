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
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultTool;
import com.fate1412.crmSystem.utils.TableResultData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<CustomerDTO> customerDTOList = getCustomerDTOList(customerList);
    
        IPage<CustomerDTO> iPage = new Page<>(thisPage,pageSize);
        iPage.setCurrent(page.getCurrent());
        iPage.setRecords(customerDTOList);
        return iPage;
    }
    
    @Override
    public List<CustomerDTO> getDTDListById(List<Long> ids) {
        List<Customer> customerList = customerMapper.selectBatchIds(ids);
        return getCustomerDTOList(customerList);
    }
    
    private List<CustomerDTO> getCustomerDTOList(List<Customer> customerList) {
        List<Long> createIds = MyCollections.objects2List(customerList, Customer::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(customerList, Customer::getUpdateMember);
        List<Long> ownerIds = MyCollections.objects2List(customerList, Customer::getOwner);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds, ownerIds);
    
        List<SysUser> sysUserList = sysUserMapper.selectBatchIds(userIdList);
    
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
    
        List<CustomerDTO> customerDTOList = MyCollections.copyListProperties(customerList, CustomerDTO::new);
        customerDTOList.forEach(customerDTO -> {
            Long createId = customerDTO.getCreater();
            Long updateMemberId = customerDTO.getUpdateMember();
            Long ownerId = customerDTO.getOwner();
            customerDTO.setCreaterR(new IdToName(createId,userMap.get(createId),"sysUser"));
            customerDTO.setUpdateMemberR(new IdToName(updateMemberId,userMap.get(updateMemberId),"sysUser"));
            customerDTO.setOwnerR(new IdToName(ownerId,userMap.get(ownerId),"sysUser"));
        });
        return customerDTOList;
    }
}