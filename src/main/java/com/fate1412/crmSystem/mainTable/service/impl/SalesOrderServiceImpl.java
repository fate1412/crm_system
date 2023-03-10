package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.dto.OptionDTO;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.child.SalesOrderChild;
import com.fate1412.crmSystem.mainTable.dto.insert.SalesOrderInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.SalesOrderSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.SalesOrderUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.CustomerMapper;
import com.fate1412.crmSystem.mainTable.pojo.*;
import com.fate1412.crmSystem.mainTable.mapper.SalesOrderMapper;
import com.fate1412.crmSystem.mainTable.service.IOrderProductService;
import com.fate1412.crmSystem.mainTable.service.IProductService;
import com.fate1412.crmSystem.mainTable.service.ISalesOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.pojo.SysUser;
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
    @Autowired
    private IProductService productService;
    @Autowired
    private IOrderProductService orderProductService;
    
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
            dto.setCreaterR(new IdToName(createId, userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updater, userMap.get(updater), TableNames.sysUser));
            dto.setCustomerIdR(new IdToName(customerId, customerMap.get(customerId), "customer"));
            dto.setInvoiceStatusR(optionsMap.get(dto.getInvoiceStatus()));
        });
        return salesOrderSelectDTOList;
    }
    
    @Override
    public BaseMapper<SalesOrder> mapper() {
        return salesOrderMapper;
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(SalesOrderUpdateDTO salesOrderUpdateDTO) {
        //获取当前登录用户
        SysUser sysUser = sysUserService.thisUser();
        
        List<SalesOrderChild> childList = salesOrderUpdateDTO.getChildList();
        if (MyCollections.isEmpty(childList)) {
        
        }
        //转换威威OrderProduct集合
        List<OrderProduct> childProducts = MyCollections.copyListProperties(childList, OrderProduct::new);
        //提取子对象中的产品id
        List<Long> productIds = MyCollections.objects2List(childList, SalesOrderChild::getProductId);
        //查询产品
        List<Product> productList = productService.listByIds(productIds);
        //获取产品价格
        Map<Long, Double> productMap = MyCollections.list2MapL(productList, Product::getId, Product::getPrice);
        
        Double originalPrice = 0d;
        Double discountPrice = 0d;
        //设置订单产品更新参数
        for (OrderProduct orderProduct : childProducts) {
            Long id = orderProduct.getProductId();
            orderProduct
                    .setSalesOrderId(salesOrderUpdateDTO.getId())
                    .setOriginalPrices(orderProduct.getProductNum() * productMap.get(id))//总价
                    .setDiscountPrices(orderProduct.getOriginalPrices() * orderProduct.getDiscount() / 100)//折后总价
                    .setUpdater(sysUser.getUserId())
                    .setUpdateTime(new Date());
    
            originalPrice += orderProduct.getOriginalPrices();
            discountPrice += orderProduct.getDiscountPrices();
        }
        //查询订单产品
        QueryWrapper<OrderProduct> qw1 = new QueryWrapper<>();
        qw1.lambda().eq(OrderProduct::getSalesOrderId,salesOrderUpdateDTO.getId());
        List<OrderProduct> orderProductList = orderProductService.list(qw1);
        
        //删除
        List<OrderProduct> delDifference = MyCollections.difference(orderProductList, childProducts, OrderProduct::getId);
        //更新
        List<OrderProduct> intersection = MyCollections.intersection(childProducts, orderProductList, OrderProduct::getId);
        //新增
        List<OrderProduct> addDifference = MyCollections.removeAll(childProducts, intersection);
    
        Double finalOriginalPrice = originalPrice;
        Double finalDiscountPrice = discountPrice;
        SalesOrder salesOrder = new SalesOrder();
        BeanUtils.copyProperties(salesOrderUpdateDTO,salesOrder);
        return update(new MyEntity<SalesOrder>(salesOrder) {
            @Override
            public SalesOrder set(SalesOrder salesOrder) {
                salesOrder
                        .setOriginalPrice(finalOriginalPrice)
                        .setDiscountPrice(finalDiscountPrice)
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return salesOrder;
            }
    
            @Override
            public ResultCode verification(SalesOrder salesOrder) {
                return super.verification(salesOrder);
            }
    
            @Override
            public boolean after(SalesOrder salesOrder) {
                //更新订单产品
                for (OrderProduct orderProduct : intersection) {
                    orderProductService.updateByEntity(orderProduct);
                }
                //删除订单产品
                List<Long> delIds = MyCollections.objects2List(delDifference, OrderProduct::getId);
                orderProductService.removeByIds(delIds);
                //新增
                for (OrderProduct orderProduct : addDifference) {
                    orderProductService.addEntity(orderProduct);
                }
                return true;
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(SalesOrderInsertDTO salesOrderInsertDTO) {
        SalesOrder salesOrder = new SalesOrder();
        BeanUtils.copyProperties(salesOrderInsertDTO,salesOrder);
        return add(new MyEntity<SalesOrder>(salesOrder) {
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
    
            @Override
            public boolean after(SalesOrder salesOrder) {
                salesOrderInsertDTO.setId(salesOrder.getId());
                SalesOrderUpdateDTO salesOrderUpdateDTO = new SalesOrderUpdateDTO();
                BeanUtils.copyProperties(salesOrderInsertDTO,salesOrderInsertDTO);
                JsonResult<?> jsonResult = updateByDTO(salesOrderUpdateDTO);
                return jsonResult.getSuccess();
            }
        });
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.salesOrder, new SalesOrderSelectDTO(), tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<SalesOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(SalesOrder::getId)
                .like(SalesOrder::getId, nameLike);
        IPage<SalesOrder> iPage = new Page<>(page, 10);
        salesOrderMapper.selectPage(iPage, queryWrapper);
        return IdToName.createList2(iPage.getRecords(), SalesOrder::getId, SalesOrder::getId);
    }
}
