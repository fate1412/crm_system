package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.dto.OptionDTO;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.select.SalesOrderSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.SalesOrderUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.CustomerMapper;
import com.fate1412.crmSystem.mainTable.pojo.*;
import com.fate1412.crmSystem.mainTable.mapper.SalesOrderMapper;
import com.fate1412.crmSystem.mainTable.service.ISalesOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.TableResultData;
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
    private ISysUserService sysUserService;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ITableOptionService tableOptionService;
    
    @Override
    public List<?> getDTOList(List<SalesOrder> salesOrderList) {
        if (MyCollections.isEmpty(salesOrderList)) {
            return new ArrayList<>();
        }
    
        List<OptionDTO> options = tableOptionService.getOptions(TableNames.salesOrder, "invoiceStatus");
        Map<Integer, String> optionsMap = MyCollections.list2MapL(options, OptionDTO::getOptionKey, OptionDTO::getOption);
        
        //员工
        List<Long> createIds = MyCollections.objects2List(salesOrderList, SalesOrder::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(salesOrderList, SalesOrder::getUpdater);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds);
    
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
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
            dto.setCreaterR(new IdToName(createId,userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updater,userMap.get(updater),TableNames.sysUser));
            dto.setCustomerIdR(new IdToName(customerId,customerMap.get(customerId),"customer"));
            dto.setInvoiceStatusR(optionsMap.get(dto.getInvoiceStatus()));
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
                SysUser sysUser = sysUserService.thisUser();
                salesOrder
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return salesOrder;
            }
        });
    }
    
    @Override
    public JsonResult<?> add(SalesOrderUpdateDTO salesOrderUpdateDTO) {
        return add(salesOrderUpdateDTO, new MyEntity<SalesOrder>(new SalesOrder()) {
            @Override
            public SalesOrder set(SalesOrder salesOrder) {
                SysUser sysUser = sysUserService.thisUser();
                salesOrder
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date())
                        .setCreater(sysUser.getUserId())
                        .setUpdater(sysUser.getUserId());
                return salesOrder;
            }
        });
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.salesOrder, new SalesOrderSelectDTO(),tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<SalesOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(SalesOrder::getId)
                .like(SalesOrder::getId,nameLike);
        IPage<SalesOrder> iPage = new Page<>(page,10);
        salesOrderMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList2(iPage.getRecords(), SalesOrder::getId, SalesOrder::getId);
    }
}
