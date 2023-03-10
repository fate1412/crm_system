package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.insert.StockListProductInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.StockListProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.StockListProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.ProductMapper;
import com.fate1412.crmSystem.mainTable.mapper.StockListMapper;
import com.fate1412.crmSystem.mainTable.pojo.*;
import com.fate1412.crmSystem.mainTable.mapper.StockListProductMapper;
import com.fate1412.crmSystem.mainTable.service.IStockListProductService;
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
            dto.setCreaterR(new IdToName(createId,userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updater,userMap.get(updater),TableNames.sysUser));
            dto.setStockListIdR(new IdToName(stockListId,stockListId.toString(),TableNames.stockList));
            dto.setProductIdR(new IdToName(productId,productMap.get(productId),TableNames.product));
        });
        return stockListProductSelectDTOList;
    }
    
    @Override
    public BaseMapper<StockListProduct> mapper() {
        return stockListProductMapper;
    }
    
    @Override
    public JsonResult<?> updateByDTO(StockListProductUpdateDTO stockListProductUpdateDTO) {
        StockListProduct stockListProduct = new StockListProduct();
        BeanUtils.copyProperties(stockListProductUpdateDTO,stockListProduct);
        return update(new MyEntity<StockListProduct>(stockListProduct) {
            @Override
            public StockListProduct set(StockListProduct stockListProduct) {
                SysUser sysUser = sysUserService.thisUser();
                stockListProduct
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return stockListProduct;
            }
        });
    }
    
    @Override
    public JsonResult<?> addDTO(StockListProductInsertDTO stockListProductInsertDTO) {
        StockListProduct stockListProduct = new StockListProduct();
        BeanUtils.copyProperties(stockListProductInsertDTO,stockListProduct);
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
        });
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.stockListProduct, new StockListProductSelectDTO(),tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<StockListProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(StockListProduct::getId)
                .like(StockListProduct::getId,nameLike);
        IPage<StockListProduct> iPage = new Page<>(page,10);
        stockListProductMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList2(iPage.getRecords(), StockListProduct::getId, StockListProduct::getId);
    }
}
