package com.fate1412.crmSystem.module.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.flow.service.ISysFlowSessionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.mainTable.dto.child.InvoiceChild;
import com.fate1412.crmSystem.module.mainTable.dto.insert.InvoiceInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.InvoiceSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.InvoiceUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.mapper.*;
import com.fate1412.crmSystem.module.mainTable.pojo.*;
import com.fate1412.crmSystem.module.mainTable.service.IInvoiceProductService;
import com.fate1412.crmSystem.module.mainTable.service.IInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.module.mainTable.service.IOrderProductService;
import com.fate1412.crmSystem.module.security.pojo.SysUser;
import com.fate1412.crmSystem.module.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private InvoiceProductMapper invoiceProductMapper;
    @Autowired
    private IInvoiceProductService invoiceProductService;
    @Autowired
    private ITableOptionService tableOptionService;
    @Autowired
    private IOrderProductService orderProductService;
    @Autowired
    private ISysFlowSessionService flowSessionService;
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(InvoiceUpdateDTO invoiceUpdateDTO) {
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceUpdateDTO,invoice);
        Invoice invoice1 = getById(invoice.getId());
        invoice.setSalesOrderId(invoice1.getSalesOrderId());
        if (invoice.getCustomerId() == null) {
            SalesOrder salesOrder = salesOrderMapper.selectById(invoice.getSalesOrderId());
            invoice.setCustomerId(salesOrder.getCustomerId());
        }
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
                return isRight(invoice);
            }
    
            @Override
            public boolean after(Invoice invoice) {
                return afterUpdateChild(invoice,invoiceUpdateDTO.getChildList());
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(InvoiceInsertDTO invoiceInsertDTO) {
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceInsertDTO,invoice);
        if (invoice.getCustomerId() == null) {
            SalesOrder salesOrder = salesOrderMapper.selectById(invoice.getSalesOrderId());
            invoice.setCustomerId(salesOrder.getCustomerId());
        }
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
                return isRight(invoice);
            }
        
            @Override
            public boolean after(Invoice invoice) {
                flowSessionService.addFlowSession(TableNames.invoice, invoice.getId());
                return afterUpdateChild(invoice,invoiceInsertDTO.getChildList());
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
    
        //审批
        List<Long> invoiceListIds = MyCollections.objects2List(invoiceList, Invoice::getId);
        Map<Long, Integer> passMap = flowSessionService.getPass(TableNames.invoice, invoiceListIds);
        
        
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
            Integer pass = passMap.get(dto.getId());
            switch (pass) {
                case 0: dto.setPass("未审批");break;
                case 1: dto.setPass("已通过");break;
                default: dto.setPass("已拒绝");
            }
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
    public <D> TableResultData getColumns(D dto) {
        return getColumns(TableNames.invoice, dto, tableOptionService);
    }
    
    @Override
    public boolean delById(Long id) {
        Invoice invoice = getById(id);
        if (invoiceProductService.delByInvoiceId(invoice)) {
            return removeById(invoice.getId());
        }
        return false;
    }
    
    @Override
    public MyPage listByPage(SelectPage<InvoiceSelectDTO> selectPage) {
        InvoiceSelectDTO like = selectPage.getLike();
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(like.getId() != null,Invoice::getId, like.getId())
                .like(like.getSalesOrderId() != null,Invoice::getSalesOrderId,like.getSalesOrderId());
        return listByPage(selectPage.getPage(),selectPage.getPageSize(),queryWrapper);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<Invoice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(Invoice::getId)
                .like(Invoice::getId,nameLike.trim());
        IPage<Invoice> iPage = new Page<>(page,10);
        invoiceMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList2(iPage.getRecords(), Invoice::getId, Invoice::getId);
    }
    
    private ResultCode isRight(Invoice invoice) {
        Invoice old = null;
        if (invoice.getId() != null) {
            old = getById(invoice.getId());
        }
        //销售订单
        if (invoice.getSalesOrderId() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        SalesOrder salesOrder = salesOrderMapper.selectById(invoice.getSalesOrderId());
        if (salesOrder == null) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //物流单号
        if (invoice.getLogisticsId()!= null && StringUtils.isBlank(invoice.getLogisticsId())) {
            return ResultCode.PARAM_IS_BLANK;
        } else if (invoice.getLogisticsId()!= null) {
            invoice.setLogisticsId(invoice.getLogisticsId().trim());
        }
        //计划发货日期
        if (invoice.getPlanInvoiceDate() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        //收货地址
        if (StringUtils.isBlank(invoice.getAddress())) {
            return ResultCode.PARAM_IS_BLANK;
        }
        invoice.setAddress(invoice.getAddress().trim());
        //收货日期
        if (old == null) {
            if (invoice.getIsInvoice() == null && invoice.getReceiptTime() != null) {
                return ResultCode.PARAM_IS_BLANK;
            } else if (invoice.getInvoiceDate() != null && invoice.getReceiptTime() != null) {
                if (invoice.getReceiptTime().before(invoice.getInvoiceDate())) {
                    return ResultCode.PARAM_NOT_VALID;
                }
            }
        } else {
            if (old.getIsInvoice() == null && invoice.getReceiptTime() != null) {
                return ResultCode.PARAM_IS_BLANK;
            } else if (old.getInvoiceDate() != null && invoice.getReceiptTime() != null) {
                if (invoice.getReceiptTime().before(old.getInvoiceDate())) {
                    return ResultCode.PARAM_NOT_VALID;
                }
            }
        }
        return ResultCode.SUCCESS;
    }
    
    private boolean afterUpdateChild(Invoice invoice, List<InvoiceChild> childList) {
        //删除所有发货单产品并生成完整发货单产品
        if (MyCollections.isEmpty(childList)) {
            invoiceProductService.delByInvoiceId(invoice);
            List<OrderProduct> orderProducts = invoiceProductMapper.getUnInvoiceNum(invoice.getSalesOrderId());
            Map<Long, Integer> unInvoiceNumMap = orderProducts.stream().collect(Collectors.toMap(OrderProduct::getProductId, (t -> t.getProductNum() - t.getInvoiceNum())));
            unInvoiceNumMap.forEach((productId,unInvoiceNum)-> {
                InvoiceProduct invoiceProduct = new InvoiceProduct();
                invoiceProduct
                        .setInvoiceId(invoice.getId())
                        .setProductId(productId)
                        .setInvoiceNum(unInvoiceNum);
                invoiceProductService.addEntity(invoiceProduct);
            });
            return true;
        }
        //合并同一个产品并转换成InvoiceProduct集合
        Map<Long, InvoiceChild> childMap = MyCollections.list2Map(childList, InvoiceChild::getProductId, (v1, v2) -> {
            if (v1.getInvoiceId() != null) {
                return v1.setInvoiceNum(v1.getInvoiceNum() + v2.getInvoiceNum());
            }
            if (v2.getInvoiceId() != null) {
                return v2.setInvoiceNum(v1.getInvoiceNum() + v2.getInvoiceNum());
            }
            return v1.setInvoiceNum(v1.getInvoiceNum() + v2.getInvoiceNum());
        });
        childList = MyCollections.map2list(childMap);
        List<InvoiceProduct> childProducts = MyCollections.copyListProperties(childList, InvoiceProduct::new);
        childProducts = childProducts.stream().peek(child -> child.setInvoiceId(invoice.getId())).collect(Collectors.toList());
        //查询发货单产品
        QueryWrapper<InvoiceProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InvoiceProduct::getInvoiceId,invoice.getId());
        List<InvoiceProduct> invoiceProductList = invoiceProductService.getByInvoiceId(invoice.getId());
    
        //删除
        List<InvoiceProduct> delDifference = MyCollections.difference(invoiceProductList, childProducts, InvoiceProduct::getId);
        //更新
        List<InvoiceProduct> intersection = MyCollections.intersection(childProducts, invoiceProductList, InvoiceProduct::getId);
        //新增
        List<InvoiceProduct> addDifference = MyCollections.removeAll(childProducts, intersection);
    
        //更新发货单产品
        for (InvoiceProduct invoiceProduct : intersection) {
            invoiceProductService.updateByEntity(invoiceProduct);
        }
        //删除发货单产品
        List<Long> delIds = MyCollections.objects2List(delDifference, InvoiceProduct::getId);
        invoiceProductService.delByIds(delIds);
        //新增发货单产品
        for (InvoiceProduct invoiceProduct : addDifference) {
            invoiceProductService.addEntity(invoiceProduct);
        }
        return true;
        
    }
}
