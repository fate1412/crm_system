package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.insert.OrderProductInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.OrderProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.OrderProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.*;
import com.fate1412.crmSystem.mainTable.pojo.OrderProduct;
import com.fate1412.crmSystem.mainTable.pojo.Product;
import com.fate1412.crmSystem.mainTable.service.IOrderProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.TableResultData;
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
 * 订单产品 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Service
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct> implements IOrderProductService {
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SalesOrderMapper salesOrderMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private ITableOptionService tableOptionService;
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(OrderProductUpdateDTO orderProductUpdateDTO) {
        OrderProduct orderProduct = new OrderProduct();
        BeanUtils.copyProperties(orderProductUpdateDTO,orderProduct);
        return updateByEntity(orderProduct);
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByEntity(OrderProduct orderProduct) {
        return update(new MyEntity<OrderProduct>(orderProduct) {
            @Override
            public OrderProduct set(OrderProduct orderProduct) {
                SysUser sysUser = sysUserService.thisUser();
                orderProduct
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return orderProduct;
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(OrderProductInsertDTO orderProductInsertDTO) {
        OrderProduct orderProduct = new OrderProduct();
        BeanUtils.copyProperties(orderProductInsertDTO,orderProduct);
        return addEntity(orderProduct);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addEntity(OrderProduct orderProduct) {
        return add(new MyEntity<OrderProduct>(orderProduct) {
            @Override
            public OrderProduct set(OrderProduct orderProduct) {
                SysUser sysUser = sysUserService.thisUser();
                orderProduct
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date())
                        .setCreater(sysUser.getUserId())
                        .setUpdater(sysUser.getUserId());
                return orderProduct;
            }
        });
    }
    
    @Override
    public List<?> getDTOList(List<OrderProduct> orderProductList) {
        if (MyCollections.isEmpty(orderProductList)) {
            return new ArrayList<>();
        }
        //员工
        List<Long> createIds = MyCollections.objects2List(orderProductList, OrderProduct::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(orderProductList, OrderProduct::getUpdater);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds);
    
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
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
            dto.setCreaterR(new IdToName(createId,userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updateMemberId,userMap.get(updateMemberId),TableNames.sysUser));
            dto.setSalesOrderIdR(new IdToName(salesOrderId, salesOrderId.toString(),"salesOrder"));
            dto.setProductIdR(new IdToName(productId,productMap.get(productId),"product"));
        
        });
        return orderProductSelectDTOList;
    }
    
    @Override
    public BaseMapper<OrderProduct> mapper() {
        return orderProductMapper;
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.orderProduct, new OrderProductSelectDTO(),tableOptionService);
    }
    
    @Override
    public <D> TableResultData getColumns(D dto) {
        return getColumns(TableNames.orderProduct, dto,tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(OrderProduct::getId)
                .like(OrderProduct::getId,nameLike);
        IPage<OrderProduct> iPage = new Page<>(page,10);
        orderProductMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList2(iPage.getRecords(), OrderProduct::getId, OrderProduct::getId);
    }
    
    @Override
    public List<OrderProductSelectDTO> getBySalesOrder(Long salesOrderId) {
        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(OrderProduct::getSalesOrderId, salesOrderId);
        List<OrderProduct> orderProducts = orderProductMapper.selectList(queryWrapper);
        List<?> dtoList = getDTOList(orderProducts);
        return dtoList.stream().map(dto -> (OrderProductSelectDTO) dto).collect(Collectors.toList());
    }
}
