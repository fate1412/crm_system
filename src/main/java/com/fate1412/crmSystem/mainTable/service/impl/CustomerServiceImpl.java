package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.customTable.dto.OptionDTO;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.CustomerSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.CustomerUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.Customer;
import com.fate1412.crmSystem.mainTable.mapper.CustomerMapper;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.mainTable.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.fate1412.crmSystem.mainTable.constant.TableNames.sysUser;

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
    private ISysUserService sysUserService;
    
    @Autowired
    private ITableOptionService tableOptionService;
    
    @Override
    public List<?> getDTOList(List<Customer> customerList) {
        if (MyCollections.isEmpty(customerList)) {
            return new ArrayList<>();
        }
        List<OptionDTO> options = tableOptionService.getOptions(TableNames.customer, "customerType");
        Map<Integer, String> optionsMap = MyCollections.list2MapL(options, OptionDTO::getOptionKey, OptionDTO::getOption);
        
        List<Long> createIds = MyCollections.objects2List(customerList, Customer::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(customerList, Customer::getUpdateMember);
        List<Long> ownerIds = MyCollections.objects2List(customerList, Customer::getOwner);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds, ownerIds);
        
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
        
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        
        List<CustomerSelectDTO> customerSelectDTOList = MyCollections.copyListProperties(customerList, CustomerSelectDTO::new);
        customerSelectDTOList.forEach(customerDTO -> {
            Long createId = customerDTO.getCreater();
            Long updateMemberId = customerDTO.getUpdateMember();
            Long ownerId = customerDTO.getOwner();
            customerDTO.setCreaterR(new IdToName(createId, userMap.get(createId), sysUser));
            customerDTO.setUpdateMemberR(new IdToName(updateMemberId, userMap.get(updateMemberId), sysUser));
            customerDTO.setOwnerR(new IdToName(ownerId, userMap.get(ownerId), sysUser));
            customerDTO.setCustomerTypeR(optionsMap.get(customerDTO.getCustomerType()));
        });
        return customerSelectDTOList;
    }
    
    @Override
    public BaseMapper<Customer> mapper() {
        return customerMapper;
    }
    
    @Override
    public JsonResult<?> updateById(CustomerUpdateDTO customerUpdateDTO) {
        return updateByDTO(customerUpdateDTO, new MyEntity<Customer>(new Customer()) {
            
            @Override
            public Customer set(Customer customer) {
                SysUser sysUser = sysUserService.thisUser();
                customer
                        .setUpdateTime(new Date())
                        .setUpdateMember(sysUser.getUserId());
                return customer;
            }
            
            @Override
            public ResultCode verification(Customer customer) {
                return ResultCode.SUCCESS;
            }
        });
    }
    
    @Override
    public JsonResult<?> add(CustomerUpdateDTO customerUpdateDTO) {
        return add(customerUpdateDTO, new MyEntity<Customer>(new Customer()) {
            
            @Override
            public Customer set(Customer customer) {
                SysUser sysUser = sysUserService.thisUser();
                customer
                        .setCreateTime(new Date())
                        .setCreater(sysUser.getUserId())
                        .setOwner(null)
                        .setUpdateTime(new Date())
                        .setUpdateMember(sysUser.getUserId());
                return customer;
            }
            
            @Override
            public ResultCode verification(Customer customer) {
                return ResultCode.SUCCESS;
            }
        });
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.customer, CustomerSelectDTO.class, tableOptionService);
    }
}
