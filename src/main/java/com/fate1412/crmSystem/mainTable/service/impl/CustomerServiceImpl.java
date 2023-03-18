package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.customTable.dto.OptionDTO;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.insert.CustomerInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.CustomerSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.CustomerUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.Customer;
import com.fate1412.crmSystem.mainTable.mapper.CustomerMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.mainTable.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
    @Transactional
    public JsonResult<?> updateByDTO(CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerUpdateDTO,customer);
        return updateByEntity(customer);
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByEntity(Customer customer) {
        return update(new MyEntity<Customer>(customer) {
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
                return isRight(customer);
            }
        });
    }
    
    @Override
    public MyPage listByPage(SelectPage<CustomerSelectDTO> selectPage) {
        CustomerSelectDTO like = selectPage.getLike();
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(like.getId() != null, Customer::getId, like.getId())
                .like(like.getName() != null, Customer::getName,like.getName());
        return listByPage(selectPage.getPage(),selectPage.getPageSize(),queryWrapper);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(CustomerInsertDTO customerInsertDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerInsertDTO,customer);
        return addEntity(customer);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addEntity(Customer customer) {
        SysUser sysUser = sysUserService.thisUser();
        return add(new MyEntity<Customer>(customer) {
        
            @Override
            public Customer set(Customer customer) {
                customer
                        .setCreateTime(new Date())
                        .setCreater(sysUser.getUserId())
                        .setUpdateTime(new Date())
                        .setUpdateMember(sysUser.getUserId());
                return customer;
            }
        
            @Override
            public ResultCode verification(Customer customer) {
                return isRight(customer);
            }
        });
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.customer, new CustomerSelectDTO(), tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(Customer::getId, Customer::getName)
                .like(Customer::getName,nameLike);
        IPage<Customer> iPage = new Page<>(page,10);
        customerMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList(iPage.getRecords(), Customer::getId, Customer::getName);
    }
    
    private ResultCode isRight(Customer customer) {
        //客户名称
        if (customer.getName().trim().isEmpty()) {
            return ResultCode.PARAM_IS_BLANK;//参数为空
        }
        //客户类型
        if (!tableOptionService.selectOptions(TableNames.customer,"customerType",customer.getCustomerType())) {
            return ResultCode.PARAM_NOT_VALID;//参数无效
        }
        //手机号(简单校验11位数字)
        if (!Pattern.matches("\\d{11}",customer.getMobile())) {
            return ResultCode.PARAM_NOT_VALID;//参数无效
        }
        //负责人
        if (customer.getOwner()!=null) {
            SysUser sysUser = sysUserService.getById(customer.getOwner());
            if (sysUser == null) {
                return ResultCode.PARAM_NOT_VALID;//参数无效
            }
        }
        return ResultCode.SUCCESS;
    }
}
