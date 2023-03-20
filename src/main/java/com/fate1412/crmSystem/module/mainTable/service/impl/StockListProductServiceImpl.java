package com.fate1412.crmSystem.module.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.mainTable.dto.insert.StockListProductInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.StockListProductSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.StockListProductUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.mapper.ProductMapper;
import com.fate1412.crmSystem.module.mainTable.mapper.StockListMapper;
import com.fate1412.crmSystem.module.mainTable.pojo.*;
import com.fate1412.crmSystem.module.mainTable.mapper.StockListProductMapper;
import com.fate1412.crmSystem.module.mainTable.service.IStockListProductService;
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

/**
 * <p>
 * 备货单产品 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Service
public class StockListProductServiceImpl extends ServiceImpl<StockListProductMapper, StockListProduct> implements IStockListProductService {
    @Autowired
    private StockListProductMapper stockListProductMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private StockListMapper stockListMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ITableOptionService tableOptionService;
    
    @Override
    public List<?> getDTOList(List<StockListProduct> stockListProductList) {
        if (MyCollections.isEmpty(stockListProductList)) {
            return new ArrayList<>();
        }
        //员工
        List<Long> createIds = MyCollections.objects2List(stockListProductList, StockListProduct::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(stockListProductList, StockListProduct::getUpdater);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds);
        
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
        
        //产品
        List<Long> ProductIdList = MyCollections.objects2List(stockListProductList, StockListProduct::getProductId);
        
        List<Product> productList = productMapper.selectBatchIds(ProductIdList);
        Map<Long, String> productMap = MyCollections.list2MapL(productList, Product::getId, Product::getName);
        
        List<StockListProductSelectDTO> stockListProductSelectDTOList = MyCollections.copyListProperties(stockListProductList, StockListProductSelectDTO::new);
        stockListProductSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updater = dto.getUpdater();
            Long stockListId = dto.getStockListId();
            Long productId = dto.getProductId();
            dto.setCreaterR(new IdToName(createId, userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updater, userMap.get(updater), TableNames.sysUser));
            dto.setStockListIdR(new IdToName(stockListId, stockListId.toString(), TableNames.stockList));
            dto.setProductIdR(new IdToName(productId, productMap.get(productId), TableNames.product));
        });
        return stockListProductSelectDTOList;
    }
    
    @Override
    public BaseMapper<StockListProduct> mapper() {
        return stockListProductMapper;
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(StockListProductUpdateDTO stockListProductUpdateDTO) {
        StockListProduct stockListProduct = new StockListProduct();
        BeanUtils.copyProperties(stockListProductUpdateDTO, stockListProduct);
        return updateByEntity(stockListProduct);
    }
    
    @Override
    @Transactional
    public JsonResult<?> addDTO(StockListProductInsertDTO stockListProductInsertDTO) {
        StockListProduct stockListProduct = new StockListProduct();
        BeanUtils.copyProperties(stockListProductInsertDTO, stockListProduct);
        return addEntity(stockListProduct);
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByEntity(StockListProduct stockListProduct) {
        StockListProduct old = getById(stockListProduct.getId());
        return update(new MyEntity<StockListProduct>(stockListProduct) {
            @Override
            public StockListProduct set(StockListProduct stockListProduct) {
                SysUser sysUser = sysUserService.thisUser();
                stockListProduct
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return stockListProduct;
            }
            
            @Override
            public ResultCode verification(StockListProduct stockListProduct) {
                return isRight(stockListProduct);
            }
            
            @Override
            public boolean after(StockListProduct stockListProduct) {
                return afterUpdate(stockListProduct, old);
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> addEntity(StockListProduct stockListProduct) {
        return add(new MyEntity<StockListProduct>(stockListProduct) {
            @Override
            public StockListProduct set(StockListProduct stockListProduct) {
                SysUser sysUser = sysUserService.thisUser();
                stockListProduct
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date())
                        .setCreater(sysUser.getUserId())
                        .setUpdater(sysUser.getUserId());
                return stockListProduct;
            }
            
            @Override
            public ResultCode verification(StockListProduct stockListProduct) {
                return isRight(stockListProduct);
            }
            
            @Override
            public boolean after(StockListProduct stockListProduct) {
                return afterUpdate(stockListProduct, null);
            }
        });
    }
    
    @Override
    public List<StockListProductSelectDTO> getDTOByStockListId(Long stockListId) {
        QueryWrapper<StockListProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StockListProduct::getStockListId, stockListId);
        List<StockListProduct> list = list(queryWrapper);
        List<?> dtoList = getDTOList(list);
        return MyCollections.copyListProperties(dtoList, StockListProductSelectDTO::new);
    }
    
    @Override
    @Transactional
    public boolean delById(Long id) {
        return delByIds(MyCollections.toList(id));
    }
    
    @Override
    @Transactional
    public boolean delByIds(List<Long> ids) {
        if (MyCollections.isEmpty(ids)) {
            return true;
        }
        List<StockListProduct> list = listByIds(ids);
        StockList stockList = stockListMapper.selectById(list.get(0).getStockListId());
        Double allPrice = list.stream().map(slp -> (slp.getStockNum() * slp.getPrice())).reduce(0d, Double::sum);
        stockList.setPrices(stockList.getPrices() - allPrice);
        if (removeByIds(ids)) {
            return stockListMapper.updateById(stockList) > 0;
        }
        return false;
    }
    
    @Override
    @Transactional
    public boolean delByStockListId(Long id) {
        QueryWrapper<StockListProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StockListProduct::getStockListId, id);
        return remove(queryWrapper);
    }
    
    @Override
    public MyPage listByPage(SelectPage<StockListProductSelectDTO> selectPage) {
        StockListProductSelectDTO like = selectPage.getLike();
        QueryWrapper<StockListProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(like.getId() != null, StockListProduct::getId, like.getId())
                .like(like.getStockListId() != null, StockListProduct::getStockListId, like.getStockListId());
        return listByPage(selectPage.getPage(), selectPage.getPageSize(), queryWrapper);
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.stockListProduct, new StockListProductSelectDTO(), tableOptionService);
    }
    
    @Override
    public <D> TableResultData getColumns(D dto) {
        return getColumns(TableNames.stockListProduct, dto, tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<StockListProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(StockListProduct::getId)
                .like(StockListProduct::getId, nameLike);
        IPage<StockListProduct> iPage = new Page<>(page, 10);
        stockListProductMapper.selectPage(iPage, queryWrapper);
        return IdToName.createList2(iPage.getRecords(), StockListProduct::getId, StockListProduct::getId);
    }
    
    private ResultCode isRight(StockListProduct stockListProduct) {
        //备货单
        if (stockListProduct.getStockListId() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        StockList stockList = stockListMapper.selectById(stockListProduct.getStockListId());
        if (stockList == null) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //产品
        if (stockListProduct.getProductId() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        Product product = productMapper.selectById(stockListProduct.getProductId());
        if (product == null) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //备货数量
        if (stockListProduct.getStockNum() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        if (stockListProduct.getStockNum() < 0) {
            return ResultCode.PARAM_NOT_VALID;
        }
        return ResultCode.SUCCESS;
    }
    
    private boolean afterUpdate(StockListProduct stockListProduct, StockListProduct old) {
        StockList stockList = stockListMapper.selectById(stockListProduct.getStockListId());
        double totalPrice = stockListProduct.getStockNum() * stockListProduct.getPrice();
        //更新备货单总价格
        if (old != null) {
            double oldTotalPrice = old.getStockNum() * old.getPrice();
            double differenceTotalPrice = totalPrice - oldTotalPrice;
            stockList.setPrices(stockList.getPrices() + differenceTotalPrice);
        } else {
            stockList.setPrices(stockList.getPrices() + totalPrice);
        }
        return stockListMapper.updateById(stockList) > 0;
    }
}
