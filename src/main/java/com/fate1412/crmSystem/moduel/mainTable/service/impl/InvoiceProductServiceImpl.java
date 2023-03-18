package com.fate1412.crmSystem.moduel.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.moduel.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.moduel.mainTable.constant.TableNames;
import com.fate1412.crmSystem.moduel.mainTable.dto.select.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.moduel.mainTable.dto.update.InvoiceProductUpdateDTO;
import com.fate1412.crmSystem.moduel.mainTable.mapper.InvoiceMapper;
import com.fate1412.crmSystem.moduel.mainTable.mapper.ProductMapper;
import com.fate1412.crmSystem.moduel.mainTable.pojo.Invoice;
import com.fate1412.crmSystem.moduel.mainTable.pojo.InvoiceProduct;
import com.fate1412.crmSystem.moduel.mainTable.mapper.InvoiceProductMapper;
import com.fate1412.crmSystem.moduel.mainTable.pojo.OrderProduct;
import com.fate1412.crmSystem.moduel.mainTable.pojo.Product;
import com.fate1412.crmSystem.moduel.mainTable.service.IInvoiceProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.moduel.mainTable.service.IOrderProductService;
import com.fate1412.crmSystem.moduel.security.pojo.SysUser;
import com.fate1412.crmSystem.moduel.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 发货单产品 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Service
public class InvoiceProductServiceImpl extends ServiceImpl<InvoiceProductMapper, InvoiceProduct> implements IInvoiceProductService {
    @Autowired
    private InvoiceMapper invoiceMapper;
    @Autowired
    private InvoiceProductMapper invoiceProductMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ITableOptionService tableOptionService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IOrderProductService orderProductService;
    
    @Override
    public JsonResult<?> updateByDTO(InvoiceProductUpdateDTO invoiceProductUpdateDTO) {
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        BeanUtils.copyProperties(invoiceProductUpdateDTO, invoiceProduct);
        return updateByEntity(invoiceProduct);
    }
    
    @Override
    public JsonResult<?> updateByEntity(InvoiceProduct invoiceProduct) {
        InvoiceProduct old = getById(invoiceProduct.getId());
        return update(new MyEntity<InvoiceProduct>(invoiceProduct) {
            @Override
            public InvoiceProduct set(InvoiceProduct invoiceProduct) {
                SysUser sysUser = sysUserService.thisUser();
                invoiceProduct
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return invoiceProduct;
            }
            
            @Override
            public ResultCode verification(InvoiceProduct invoiceProduct) {
                return isRight(invoiceProduct);
            }
            
            @Override
            public boolean after(InvoiceProduct invoiceProduct) {
                return afterUpdate(invoiceProduct);
            }
        });
    }
    
    @Override
    public JsonResult<?> addDTO(InvoiceProductSelectDTO invoiceProductSelectDTO) {
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        BeanUtils.copyProperties(invoiceProductSelectDTO, invoiceProduct);
        return addEntity(invoiceProduct);
    }
    
    @Override
    public JsonResult<?> addEntity(InvoiceProduct invoiceProduct) {
        return add(new MyEntity<InvoiceProduct>(invoiceProduct) {
            @Override
            public InvoiceProduct set(InvoiceProduct invoiceProduct) {
                SysUser sysUser = sysUserService.thisUser();
                invoiceProduct
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date())
                        .setCreater(sysUser.getUserId())
                        .setUpdater(sysUser.getUserId());
                return invoiceProduct;
            }
            
            @Override
            public ResultCode verification(InvoiceProduct invoiceProduct) {
                return isRight(invoiceProduct);
            }
            
            @Override
            public boolean after(InvoiceProduct invoiceProduct) {
                return afterUpdate(invoiceProduct);
            }
        });
    }
    
    @Override
    public List<InvoiceProductSelectDTO> getDTOByInvoiceId(Long invoiceId) {
        List<InvoiceProduct> invoiceProductList = getByInvoiceId(invoiceId);
        List<?> dtoList = getDTOList(invoiceProductList);
        return MyCollections.copyListProperties(dtoList, InvoiceProductSelectDTO::new);
    }
    
    @Override
    public List<InvoiceProduct> getByInvoiceId(Long invoiceId) {
        QueryWrapper<InvoiceProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(InvoiceProduct::getInvoiceId, invoiceId);
        return list(queryWrapper);
    }
    
    @Override
    public boolean delById(Long id) {
        return delByIds(MyCollections.toList(id));
    }
    
    @Override
    public boolean delByIds(List<Long> ids) {
        if (MyCollections.isEmpty(ids)) {
            return true;
        }
        List<InvoiceProduct> list = listByIds(ids);
        Map<Long, Integer> map = MyCollections.list2MapL(list, InvoiceProduct::getProductId, InvoiceProduct::getInvoiceNum);
        Invoice invoice = invoiceMapper.selectById(list.get(0).getInvoiceId());
        removeByIds(ids);
        return updateOrderProduct(map, invoice);
    }
    
    @Override
    public boolean delByInvoiceId(Invoice invoice) {
        QueryWrapper<InvoiceProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(InvoiceProduct::getInvoiceId, invoice.getId());
        List<InvoiceProduct> list = list(queryWrapper);
        remove(queryWrapper);
        Map<Long, Integer> map = MyCollections.list2MapL(list, InvoiceProduct::getProductId, InvoiceProduct::getInvoiceNum);
        return updateOrderProduct(map, invoice);
    }
    
    @Override
    public MyPage listByPage(SelectPage<InvoiceProductSelectDTO> selectPage) {
        InvoiceProductSelectDTO like = selectPage.getLike();
        QueryWrapper<InvoiceProduct> queryWrapper =new QueryWrapper<>();
        queryWrapper.lambda()
                .like(like.getId() != null,InvoiceProduct::getId, like.getId())
                .like(like.getInvoiceId() != null,InvoiceProduct::getInvoiceId, like.getInvoiceId())
                .like(like.getProductId() != null,InvoiceProduct::getProductId, like.getProductId());
        return listByPage(selectPage.getPage(),selectPage.getPageSize(),queryWrapper);
    }
    
    private boolean updateOrderProduct(Map<Long, Integer> map, Invoice invoice) {
        List<OrderProduct> orderProductList = orderProductService.getBySalesOrderId(invoice.getSalesOrderId());
        orderProductList = orderProductList.stream().peek(orderProduct -> {
            Integer invoiceNum = map.get(orderProduct.getProductId());
            if (invoiceNum != null) {
                if (invoiceNum >= orderProduct.getInvoiceNum()) {
                    orderProduct.setInvoiceNum(0);
                    invoiceNum = invoiceNum - orderProduct.getInvoiceNum();
                    map.put(orderProduct.getProductId(), invoiceNum);
                } else {
                    orderProduct.setInvoiceNum(orderProduct.getInvoiceNum() - invoiceNum);
                    map.put(orderProduct.getProductId(), 0);
                }
            }
            
        }).collect(Collectors.toList());
        return orderProductService.updateBatchById(orderProductList);
    }
    
    @Override
    public List<?> getDTOList(List<InvoiceProduct> invoiceProductList) {
        if (MyCollections.isEmpty(invoiceProductList)) {
            return new ArrayList<>();
        }
        //员工
        List<Long> createIds = MyCollections.objects2List(invoiceProductList, InvoiceProduct::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(invoiceProductList, InvoiceProduct::getUpdater);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds);
        
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        
        //产品
        List<Long> ProductIdList = MyCollections.objects2List(invoiceProductList, InvoiceProduct::getProductId);
        
        List<Product> productList = productMapper.selectBatchIds(ProductIdList);
        Map<Long, String> productMap = MyCollections.list2MapL(productList, Product::getId, Product::getName);
        
        
        List<InvoiceProductSelectDTO> invoiceProductSelectDTOList = MyCollections.copyListProperties(invoiceProductList, InvoiceProductSelectDTO::new);
        invoiceProductSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updateMemberId = dto.getUpdater();
            Long productId = dto.getProductId();
            Long invoiceId = dto.getInvoiceId();
            dto.setCreaterR(new IdToName(createId, userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updateMemberId, userMap.get(updateMemberId), TableNames.sysUser));
            dto.setInvoiceIdR(new IdToName(invoiceId, invoiceId.toString(), TableNames.invoice));
            dto.setProductIdR(new IdToName(productId, productMap.get(productId), TableNames.product));
            
        });
        return invoiceProductSelectDTOList;
    }
    
    @Override
    public BaseMapper<InvoiceProduct> mapper() {
        return invoiceProductMapper;
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.invoiceProduct, new InvoiceProductSelectDTO(), tableOptionService);
    }
    
    @Override
    public <D> TableResultData getColumns(D dto) {
        return getColumns(TableNames.invoiceProduct, dto, tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<InvoiceProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(InvoiceProduct::getId)
                .like(InvoiceProduct::getId, nameLike);
        IPage<InvoiceProduct> iPage = new Page<>(page, 10);
        invoiceProductMapper.selectPage(iPage, queryWrapper);
        return IdToName.createList2(iPage.getRecords(), InvoiceProduct::getId, InvoiceProduct::getId);
    }
    
    private ResultCode isRight(InvoiceProduct invoiceProduct) {
        InvoiceProduct old = null;
        if (invoiceProduct.getId() != null) {
            old = getById(invoiceProduct.getId());
        }
        //关联发货单
        if (invoiceProduct.getInvoiceId() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        Invoice invoice = invoiceMapper.selectById(invoiceProduct.getInvoiceId());
        if (invoice == null) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //关联产品
        if (invoiceProduct.getProductId() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        //确保该产品在对应的订单产品中
        List<OrderProduct> orderProductList = invoiceProductMapper.getOrderProductList(invoice.getSalesOrderId(), invoiceProduct.getProductId());
        if (MyCollections.isEmpty(orderProductList)) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //发货数量
        if (invoiceProduct.getInvoiceNum() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        List<OrderProduct> orderProducts = invoiceProductMapper.getUnInvoiceNum(invoice.getSalesOrderId());
        Map<Long, Integer> unInvoiceNumMap = orderProducts.stream().collect(Collectors.toMap(OrderProduct::getProductId, (t -> t.getProductNum() - t.getInvoiceNum())));
        Integer unInvoiceNum = unInvoiceNumMap.get(invoiceProduct.getProductId());
        //新增时发货数量判定
        if (old == null && invoiceProduct.getInvoiceNum() > unInvoiceNum) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //更新时发货数量判定
        if (old != null && invoiceProduct.getInvoiceNum() > (unInvoiceNum + old.getInvoiceNum())) {
            return ResultCode.PARAM_NOT_VALID;
        }
        return ResultCode.SUCCESS;
    }
    
    private boolean afterUpdate(InvoiceProduct invoiceProduct) {
        Invoice invoice = invoiceMapper.selectById(invoiceProduct.getInvoiceId());
        //获取对应的订单产品
        List<OrderProduct> orderProductList = invoiceProductMapper.getOrderProductList(invoice.getSalesOrderId(), invoiceProduct.getProductId());
        //获取该订单下的其他发货单产品(当前发货单除外)
        List<InvoiceProduct> invoiceProductList = invoiceProductMapper.getInvoiceProductList(invoice.getSalesOrderId(), MyCollections.toList(invoice.getId()));
        Map<Long, Integer> map = null;
        if (!MyCollections.isEmpty(invoiceProductList)) {
            map = MyCollections.list2Map(invoiceProductList, InvoiceProduct::getProductId, InvoiceProduct::getInvoiceNum, Integer::sum);
        }

        //由于一个发货单只能有一个相同的产品，因此已发货数重设即可
        Integer invoiceNum = invoiceProduct.getInvoiceNum();
        for (OrderProduct orderProduct : orderProductList) {
            Integer unInvoiceNum = orderProduct.getProductNum();
            Integer oldInvoiceNum = 0;
            if (!MyCollections.isEmpty(map)) {
                unInvoiceNum = orderProduct.getProductNum() - oldInvoiceNum;
                oldInvoiceNum = map.get(orderProduct.getProductId());
            }
            
            if (invoiceNum - unInvoiceNum >= 0) {
                orderProduct.setInvoiceNum(orderProduct.getProductNum());
                invoiceNum = invoiceNum - unInvoiceNum;
            } else {
                orderProduct.setInvoiceNum(oldInvoiceNum + invoiceNum);
                invoiceNum = 0;
            }
            orderProductService.updateByEntity(orderProduct);
            
        }
        return true;
    }
    
}
