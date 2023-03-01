package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.mainTable.dto.InvoiceSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.CustomerMapper;
import com.fate1412.crmSystem.mainTable.mapper.SalesOrderMapper;
import com.fate1412.crmSystem.mainTable.pojo.Customer;
import com.fate1412.crmSystem.mainTable.pojo.Invoice;
import com.fate1412.crmSystem.mainTable.mapper.InvoiceMapper;
import com.fate1412.crmSystem.mainTable.pojo.SalesOrder;
import com.fate1412.crmSystem.mainTable.service.IInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.MyCollections;
import org.springframework.beans.BeanUtils;
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
    public IPage<InvoiceSelectDTO> listByPage(long thisPage, long pageSize) {
        IPage<Invoice> page = new Page<>(thisPage,pageSize);
        invoiceMapper.selectPage(page,null);
        List<Invoice> customerList = page.getRecords();
        List<InvoiceSelectDTO> customerDTOList = getInvoiceDTOList(customerList);
    
        IPage<InvoiceSelectDTO> iPage = new Page<>(thisPage,pageSize);
        iPage.setCurrent(page.getCurrent());
        iPage.setRecords(customerDTOList);
        return iPage;
    }
    
    @Override
    public List<InvoiceSelectDTO> getDTOListById(List<Long> ids) {
        List<Invoice> customerList = invoiceMapper.selectBatchIds(ids);
        return getInvoiceDTOList(customerList);
    }
    
    @Override
    public boolean updateById(InvoiceUpdateDTO invoiceUpdateDTO) {
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceUpdateDTO,invoice);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        SysUser sysUser = sysUserMapper.getByUserName(authentication.getName());
        invoice
                .setUpdateTime(new Date());
//                .setUpdateMember(sysUser.getUserId());
        return invoiceMapper.updateById(invoice) > 0;
    }
    
    @Override
    public boolean add(InvoiceSelectDTO invoiceSelectDTO) {
        Invoice invoice = new Invoice();
        invoice.setId(null);
        BeanUtils.copyProperties(invoiceSelectDTO,invoice);
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        SysUser sysUser = sysUserMapper.getByUserName(user.getUsername());
        invoice
                .setCreateTime(new Date())
//                .setCreater(sysUser.getUserId())
//                .setOwner(null)
                .setUpdateTime(new Date());
//                .setUpdateMember(sysUser.getUserId());
        return invoiceMapper.insert(invoice) > 0;
    }
    
    private List<InvoiceSelectDTO> getInvoiceDTOList(List<Invoice> invoiceList) {
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
            dto.setSalesOrderIdR(new IdToName(salesOrderId, salesOrderId.toString(),"SalesOrder"));
            dto.setCustomerR(new IdToName(customerId,customerMap.get(customerId),"customer"));

        });
        return invoiceSelectDTOList;
    }
}
