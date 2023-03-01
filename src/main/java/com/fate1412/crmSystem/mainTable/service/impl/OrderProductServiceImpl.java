package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.OrderProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.OrderProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.*;
import com.fate1412.crmSystem.mainTable.pojo.InvoiceProduct;
import com.fate1412.crmSystem.mainTable.pojo.OrderProduct;
import com.fate1412.crmSystem.mainTable.pojo.Product;
import com.fate1412.crmSystem.mainTable.service.IOrderProductService;
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
 * 订单产品 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Service
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct> implements IOrderProductService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    
    
    @Override
    public IPage<OrderProductSelectDTO> listByPage(long thisPage, long pageSize) {
        IPage<OrderProduct> page = new Page<>(thisPage,pageSize);
        orderProductMapper.selectPage(page,null);
        List<OrderProduct> orderProductList = page.getRecords();
        List<OrderProductSelectDTO> orderProductSelectDTOList = getInvoiceDTOList(orderProductList);
    
        IPage<OrderProductSelectDTO> iPage = new Page<>(thisPage,pageSize);
        iPage.setCurrent(page.getCurrent());
        iPage.setRecords(orderProductSelectDTOList);
        return iPage;
    }
    
    @Override
    public List<OrderProductSelectDTO> getDTOListById(List<Long> ids) {
        List<OrderProduct> orderProductList = orderProductMapper.selectBatchIds(ids);
        return getInvoiceDTOList(orderProductList);
    }
    
    @Override
    public boolean updateById(OrderProductUpdateDTO orderProductUpdateDTO) {
        OrderProduct orderProduct = new OrderProduct();
        BeanUtils.copyProperties(orderProductUpdateDTO,orderProduct);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        SysUser sysUser = sysUserMapper.getByUserName(authentication.getName());
        orderProduct
                .setUpdateTime(new Date());
//                .setUpdateMember(sysUser.getUserId());
        return orderProductMapper.updateById(orderProduct) > 0;
    }
    
    @Override
    public boolean add(OrderProductSelectDTO orderProductSelectDTO) {
        OrderProduct orderProduct = new OrderProduct();
        BeanUtils.copyProperties(orderProductSelectDTO,orderProduct);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        SysUser sysUser = sysUserMapper.getByUserName(authentication.getName());
        orderProduct
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
//                .setUpdateMember(sysUser.getUserId());
        return orderProductMapper.updateById(orderProduct) > 0;
    }
    
    private List<OrderProductSelectDTO> getInvoiceDTOList(List<OrderProduct> orderProductList) {
        if (MyCollections.isEmpty(orderProductList)) {
            return new ArrayList<>();
        }
        //员工
        List<Long> createIds = MyCollections.objects2List(orderProductList, OrderProduct::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(orderProductList, OrderProduct::getUpdater);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds);
        
        List<SysUser> sysUserList = sysUserMapper.selectBatchIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        
        //产品
        List<Long> ProductIdList = MyCollections.objects2List(orderProductList, OrderProduct::getProductId);
        
        List<Product> productList = productMapper.selectBatchIds(ProductIdList);
        Map<Long, String> productMap = MyCollections.list2MapL(productList, Product::getId, Product::getName);
        
        
        List<OrderProductSelectDTO> orderProductSelectDTOList = MyCollections.copyListProperties(orderProductList, OrderProductSelectDTO::new);
        orderProductSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updateMemberId = dto.getUpdater();
            Long productId = dto.getProductId();
            Long salesOrderId = dto.getSalesOrderId();
            dto.setCreaterR(new IdToName(createId,userMap.get(createId),"sysUser"));
            dto.setUpdaterR(new IdToName(updateMemberId,userMap.get(updateMemberId),"sysUser"));
            dto.setSalesOrderR(new IdToName(salesOrderId, salesOrderId.toString(),"salesOrder"));
            dto.setProductR(new IdToName(productId,productMap.get(productId),"product"));
            
        });
        return orderProductSelectDTOList;
    }
}
