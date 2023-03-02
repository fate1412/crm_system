package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.mainTable.dto.InvoiceSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.CustomerMapper;
import com.fate1412.crmSystem.mainTable.mapper.SalesOrderMapper;
import com.fate1412.crmSystem.mainTable.pojo.Customer;
import com.fate1412.crmSystem.mainTable.pojo.Invoice;
import com.fate1412.crmSystem.mainTable.mapper.InvoiceMapper;
import com.fate1412.crmSystem.mainTable.service.IInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 发货单 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements IInvoiceService {
    @Autowired
    private InvoiceMapper invoiceMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private CustomerMapper customerMapper;
    
    @Override
    public JsonResult<?> updateById(InvoiceUpdateDTO invoiceUpdateDTO) {
        return updateByDTO(invoiceUpdateDTO, new MyEntity<Invoice>(new Invoice()) {
            @Override
            public Invoice set(Invoice invoice) {
                invoice
                        .setUpdateTime(new Date());
//                .setUpdateMember(sysUser.getUserId());
                return invoice;
            }
    
            @Override
            public ResultCode verification(Invoice invoice) {
                return ResultCode.SUCCESS;
            }
        });
    }
    
    @Override
    public JsonResult<?> add(InvoiceSelectDTO invoiceSelectDTO) {
        return add(invoiceSelectDTO, new MyEntity<Invoice>(new Invoice()) {
            @Override
            public Invoice set(Invoice invoice) {
                invoice
                        .setCreateTime(new Date())
//                .setCreater(sysUser.getUserId())
//                .setOwner(null)
                        .setUpdateTime(new Date());
//                .setUpdateMember(sysUser.getUserId());
                return invoice;
            }
    
            @Override
            public ResultCode verification(Invoice invoice) {
                return ResultCode.SUCCESS;
            }
        });
    }
    
    @Override
    public List<?> getDTOList(List<Invoice> invoiceList) {
        if (MyCollections.isEmpty(invoiceList)) {
            return new ArrayList<>();
        }
        //员工
        List<Long> createIds = MyCollections.objects2List(invoiceList, Invoice::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(invoiceList, Invoice::getUpdater);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds);
    
        List<SysUser> sysUserList = sysUserMapper.selectBatchIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
    
        //客户
        List<Long> salesOrderIdList = MyCollections.objects2List(invoiceList, Invoice::getCustomerId);
    
        List<Customer> customerList = customerMapper.selectBatchIds(salesOrderIdList);
        Map<Long, String> customerMap = MyCollections.list2MapL(customerList, Customer::getId, Customer::getName);
    
    
        List<InvoiceSelectDTO> invoiceSelectDTOList = MyCollections.copyListProperties(invoiceList, InvoiceSelectDTO::new);
        invoiceSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updateMemberId = dto.getUpdater();
            Long salesOrderId = dto.getSalesOrderId();
            Long customerId = dto.getCustomerId();
            dto.setCreaterR(new IdToName(createId,userMap.get(createId),"sysUser"));
            dto.setUpdaterR(new IdToName(updateMemberId,userMap.get(updateMemberId),"sysUser"));
            dto.setSalesOrderIdR(new IdToName(salesOrderId, salesOrderId.toString(),"salesOrder"));
            dto.setCustomerR(new IdToName(customerId,customerMap.get(customerId),"customer"));
        
        });
        return invoiceSelectDTOList;
    }
    
    @Override
    public BaseMapper<Invoice> mapper() {
        return invoiceMapper;
    }
}
