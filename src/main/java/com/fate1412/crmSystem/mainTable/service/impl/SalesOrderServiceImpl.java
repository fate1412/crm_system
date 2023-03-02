package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.mainTable.dto.ProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.SalesOrderSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.SalesOrderUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.CustomerMapper;
import com.fate1412.crmSystem.mainTable.pojo.Customer;
import com.fate1412.crmSystem.mainTable.pojo.Invoice;
import com.fate1412.crmSystem.mainTable.pojo.Product;
import com.fate1412.crmSystem.mainTable.pojo.SalesOrder;
import com.fate1412.crmSystem.mainTable.mapper.SalesOrderMapper;
import com.fate1412.crmSystem.mainTable.service.ISalesOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 销售订单 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Service
public class SalesOrderServiceImpl extends ServiceImpl<SalesOrderMapper, SalesOrder> implements ISalesOrderService {
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private CustomerMapper customerMapper;
    
    @Override
    public List<?> getDTOList(List<SalesOrder> salesOrderList) {
        if (MyCollections.isEmpty(salesOrderList)) {
            return new ArrayList<>();
        }
        //员工
        List<Long> createIds = MyCollections.objects2List(salesOrderList, SalesOrder::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(salesOrderList, SalesOrder::getUpdater);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds);
    
        List<SysUser> sysUserList = sysUserMapper.selectBatchIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
    
    
        //客户
        List<Long> salesOrderIdList = MyCollections.objects2List(salesOrderList, SalesOrder::getCustomerId);
    
        List<Customer> customerList = customerMapper.selectBatchIds(salesOrderIdList);
        Map<Long, String> customerMap = MyCollections.list2MapL(customerList, Customer::getId, Customer::getName);
    
        List<SalesOrderSelectDTO> salesOrderSelectDTOList = MyCollections.copyListProperties(salesOrderList, SalesOrderSelectDTO::new);
        salesOrderSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updater = dto.getUpdater();
            Long customerId = dto.getCustomerId();
            dto.setCreaterR(new IdToName(createId,userMap.get(createId),"sysUser"));
            dto.setUpdaterR(new IdToName(updater,userMap.get(updater),"sysUser"));
            dto.setCustomerR(new IdToName(customerId,customerMap.get(customerId),"customer"));
        });
        return salesOrderSelectDTOList;
    }
    
    @Override
    public BaseMapper<SalesOrder> mapper() {
        return salesOrderMapper;
    }
    
    @Override
    public JsonResult<?> updateById(SalesOrderUpdateDTO salesOrderUpdateDTO) {
        return updateByDTO(salesOrderUpdateDTO, new MyEntity<SalesOrder>(new SalesOrder()) {
            @Override
            public SalesOrder set(SalesOrder salesOrder) {
                salesOrder
                        .setUpdateTime(new Date());
                return salesOrder;
            }
        });
    }
    
    @Override
    public JsonResult<?> add(SalesOrderUpdateDTO salesOrderUpdateDTO) {
        return add(salesOrderUpdateDTO, new MyEntity<SalesOrder>(new SalesOrder()) {
            @Override
            public SalesOrder set(SalesOrder salesOrder) {
                salesOrder
                        .setCreateTime(new Date());
                return salesOrder;
            }
        });
    }
}
