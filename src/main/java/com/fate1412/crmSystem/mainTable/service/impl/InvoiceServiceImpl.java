package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.select.InvoiceSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.InvoiceUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.CustomerMapper;
import com.fate1412.crmSystem.mainTable.mapper.SalesOrderMapper;
import com.fate1412.crmSystem.mainTable.pojo.Customer;
import com.fate1412.crmSystem.mainTable.pojo.Invoice;
import com.fate1412.crmSystem.mainTable.mapper.InvoiceMapper;
import com.fate1412.crmSystem.mainTable.service.IInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ISysUserService sysUserService;
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private CustomerMapper customerMapper;
    
    @Autowired
    private ITableOptionService tableOptionService;
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(InvoiceUpdateDTO invoiceUpdateDTO) {
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceUpdateDTO,invoice);
        return updateEntity(invoice);
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateEntity(Invoice invoice) {
        return update(new MyEntity<Invoice>(invoice) {
            @Override
            public Invoice set(Invoice invoice) {
                SysUser sysUser = sysUserService.thisUser();
                invoice
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return invoice;
            }
        
            @Override
            public ResultCode verification(Invoice invoice) {
                return ResultCode.SUCCESS;
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(InvoiceSelectDTO invoiceSelectDTO) {
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceSelectDTO,invoice);
        return addEntity(invoice);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addEntity(Invoice invoice) {
        return add(new MyEntity<Invoice>(invoice) {
            @Override
            public Invoice set(Invoice invoice) {
                SysUser sysUser = sysUserService.thisUser();
                invoice
                        .setCreateTime(new Date())
                        .setCreater(sysUser.getUserId())
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
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
        
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
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
            dto.setCreaterR(new IdToName(createId, userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updateMemberId, userMap.get(updateMemberId), TableNames.sysUser));
            dto.setSalesOrderIdR(new IdToName(salesOrderId, salesOrderId.toString(), TableNames.salesOrder));
            dto.setCustomerIdR(new IdToName(customerId, customerMap.get(customerId), TableNames.customer));
            
        });
        return invoiceSelectDTOList;
    }
    
    @Override
    public BaseMapper<Invoice> mapper() {
        return invoiceMapper;
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.invoice, new InvoiceSelectDTO(), tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(Invoice::getId)
                .like(Invoice::getId,nameLike);
        IPage<Invoice> iPage = new Page<>(page,10);
        invoiceMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList2(iPage.getRecords(), Invoice::getId, Invoice::getId);
    }
}
