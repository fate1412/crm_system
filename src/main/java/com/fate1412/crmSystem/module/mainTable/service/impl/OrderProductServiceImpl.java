package com.fate1412.crmSystem.module.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.mainTable.dto.insert.OrderProductInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.OrderProductSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.OrderProductUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.mapper.*;
import com.fate1412.crmSystem.module.mainTable.pojo.OrderProduct;
import com.fate1412.crmSystem.module.mainTable.pojo.Product;
import com.fate1412.crmSystem.module.mainTable.pojo.SalesOrder;
import com.fate1412.crmSystem.module.mainTable.service.IOrderProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.module.security.pojo.SysUser;
import com.fate1412.crmSystem.module.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
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
        SysUser sysUser = sysUserService.thisUser();
        //查询产品
        Product product = productMapper.selectById(orderProduct.getProductId());
        return update(new MyEntity<OrderProduct>(orderProduct) {
            @Override
            public OrderProduct set(OrderProduct orderProduct) {
                orderProduct
                        .setOriginalPrices(orderProduct.getProductNum() * product.getPrice())//总价
                        .setDiscountPrices(orderProduct.getOriginalPrices() * orderProduct.getDiscount() / 100)//折后总价
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return orderProduct;
            }
    
            @Override
            public ResultCode verification(OrderProduct orderProduct) {
                return isRight(orderProduct);
            }
    
            @Override
            public boolean after(OrderProduct orderProduct) {
                return afterUpdateSalesOrder(orderProduct.getSalesOrderId());
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
        SysUser sysUser = sysUserService.thisUser();
        //查询产品
        Product product = productMapper.selectById(orderProduct.getProductId());
        return add(new MyEntity<OrderProduct>(orderProduct) {
            @Override
            public OrderProduct set(OrderProduct orderProduct) {
                orderProduct
                        .setOriginalPrices(orderProduct.getProductNum() * product.getPrice())//总价
                        .setDiscountPrices(orderProduct.getOriginalPrices() * orderProduct.getDiscount() / 100)//折后总价
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date())
                        .setCreater(sysUser.getUserId())
                        .setUpdater(sysUser.getUserId());
                return orderProduct;
            }
    
            @Override
            public ResultCode verification(OrderProduct orderProduct) {
                return isRight(orderProduct);
            }
    
            @Override
            public boolean after(OrderProduct orderProduct) {
                return afterUpdateSalesOrder(orderProduct.getSalesOrderId());
            }
        });
    }
    
    @Override
    public boolean delById(Long id) {
        OrderProduct orderProduct = getById(id);
        if (removeById(id)) {
            return afterUpdateSalesOrder(orderProduct.getSalesOrderId());
        }
        return false;
    }
    
    @Override
    public boolean delByIds(List<Long> ids) {
        if (MyCollections.isEmpty(ids)) {
            return true;
        }
        List<OrderProduct> orderProductList = listByIds(ids);
        if (removeByIds(ids)) {
            return afterUpdateSalesOrder(orderProductList.get(0).getSalesOrderId());
        }
        return false;
    }
    
    @Override
    public boolean delBySalesOrderId(Long id) {
        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderProduct::getSalesOrderId,id);
        remove(queryWrapper);
        return afterUpdateSalesOrder(id);
    }
    
    @Override
    public MyPage listByPage(SelectPage<OrderProductSelectDTO> selectPage) {
        OrderProductSelectDTO like = selectPage.getLike();
        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(like.getId() != null, OrderProduct::getId,like.getId())
                .like(like.getSalesOrderId() != null, OrderProduct::getSalesOrderId,like.getSalesOrderId())
                .like(like.getProductId() != null, OrderProduct::getProductId,like.getProductId());
        return listByPage(selectPage.getPage(),selectPage.getPageSize(),queryWrapper);
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
                .like(OrderProduct::getId,nameLike.trim());
        IPage<OrderProduct> iPage = new Page<>(page,10);
        orderProductMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList2(iPage.getRecords(), OrderProduct::getId, OrderProduct::getId);
    }
    
    @Override
    public List<OrderProductSelectDTO> getDTOBySalesOrderId(Long salesOrderId) {
        List<OrderProduct> orderProducts = getBySalesOrderId(salesOrderId);
        List<?> dtoList = getDTOList(orderProducts);
        return dtoList.stream().map(dto -> (OrderProductSelectDTO) dto).collect(Collectors.toList());
    }
    
    @Override
    public List<OrderProduct> getBySalesOrderId(Long salesOrderId) {
        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(OrderProduct::getSalesOrderId, salesOrderId);
        return orderProductMapper.selectList(queryWrapper);
    }
    
    private ResultCode isRight(OrderProduct orderProduct) {
        //关联产品
        if (orderProduct.getProductId() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        Product product = productMapper.selectById(orderProduct.getProductId());
        if (product == null) {
            return ResultCode.PARAM_NOT_VALID;
        }
        Integer stock = product.getStock();//库存
        //关联订单
        if (orderProduct.getSalesOrderId() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        SalesOrder salesOrder = salesOrderMapper.selectById(orderProduct.getSalesOrderId());
        if (salesOrder == null) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //购买数量
        if (orderProduct.getProductNum() <= 0 || orderProduct.getProductNum() > stock) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //发货数量
        if (orderProduct.getInvoiceNum() != null && (orderProduct.getInvoiceNum() < 0 || orderProduct.getInvoiceNum() > orderProduct.getProductNum())) {
            return ResultCode.PARAM_NOT_VALID;
        }
        return ResultCode.SUCCESS;
    }
    
    private boolean  afterUpdateSalesOrder(Long salesOrderId) {
        List<OrderProductSelectDTO> salesOrderDTOList = getDTOBySalesOrderId(salesOrderId);
        if (MyCollections.isEmpty(salesOrderDTOList)) {
            SalesOrder salesOrder = new SalesOrder();
            salesOrder
                    .setId(salesOrderId)
                    .setOriginalPrice(0D)
                    .setDiscountPrice(0D);
            return salesOrderMapper.updateById(salesOrder) > 0;
        }
        Double originalPrice = 0d;
        Double discountPrice = 0d;
        //重新计算总价
        for (OrderProductSelectDTO dto : salesOrderDTOList) {
            originalPrice += dto.getOriginalPrices();
            discountPrice += dto.getDiscountPrices();
        }
        SalesOrder salesOrder = new SalesOrder();
        salesOrder
                .setId(salesOrderId)
                .setOriginalPrice(originalPrice)
                .setDiscountPrice(discountPrice);
        return salesOrderMapper.updateById(salesOrder) > 0;
    }
}
