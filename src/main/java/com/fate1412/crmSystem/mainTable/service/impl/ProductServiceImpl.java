package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.insert.ProductInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.ProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.ProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.Product;
import com.fate1412.crmSystem.mainTable.mapper.ProductMapper;
import com.fate1412.crmSystem.mainTable.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ITableOptionService tableOptionService;
    
    @Override
    public List<?> getDTOList(List<Product> productList) {
        if (MyCollections.isEmpty(productList)) {
            return new ArrayList<>();
        }
        //员工
        List<Long> createIds = MyCollections.objects2List(productList, Product::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(productList, Product::getUpdater);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds);
    
        List<SysUser> sysUserList = sysUserService.listByIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
    
        List<ProductSelectDTO> productSelectDTOList = MyCollections.copyListProperties(productList, ProductSelectDTO::new);
        productSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updater = dto.getUpdater();
            dto.setCreaterR(new IdToName(createId,userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updater,userMap.get(updater),TableNames.sysUser));
        });
        return productSelectDTOList;
    }
    
    @Override
    public BaseMapper<Product> mapper() {
        return productMapper;
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateById(ProductUpdateDTO productUpdateDTO) {
        return updateByDTO(productUpdateDTO, new MyEntity<Product>(new Product()) {
            @Override
            public Product set(Product product) {
                SysUser sysUser = sysUserService.thisUser();
                Product product2 = getById(productUpdateDTO.getId());
                product
                        .setIsShelf()
                        .setRealStock(product2.getRealStock())//设置此值用于校验
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId());
                return product;
            }
    
            @Override
            public ResultCode verification(Product product) {
                return isRight(product);
            }
        });
    }
    
    @Override
    @Transactional
    public JsonResult<?> add(ProductInsertDTO productInsertDTO) {
        return add(productInsertDTO, new MyEntity<Product>(new Product()) {
            @Override
            public Product set(Product product) {
                SysUser sysUser = sysUserService.thisUser();
                product
                        .setIsShelf()
                        .setRealStock(product.getStock())
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date())
                        .setUpdater(sysUser.getUserId())
                        .setCreater(sysUser.getUserId());
                return product;
            }
    
            @Override
            public ResultCode verification(Product product) {
                return isRight(product);
            }
        });
    }
    
    @Override
    public TableResultData getColumns() {
        return getColumns(TableNames.product, new ProductSelectDTO(),tableOptionService);
    }
    
    @Override
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(Product::getId, Product::getName)
                .like(Product::getName,nameLike);
        IPage<Product> iPage = new Page<>(page,10);
        productMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList(iPage.getRecords(), Product::getId, Product::getName);
    }
    
    private ResultCode isRight(Product product) {
        //产品名称
        if (product.getName().isEmpty()) {
            return ResultCode.PARAM_IS_BLANK;
        }
        //单价
        if (product.getPrice() <= 0) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //库存
        if (product.getStock() < 0 || product.getStock() > product.getRealStock()){
            return ResultCode.PARAM_NOT_VALID;
        }
        //折扣
        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //上下架时间
        if (product.getOnShelfTime().after(product.getOffShelfTime())) {
            return ResultCode.PARAM_NOT_VALID;
        }
        return ResultCode.SUCCESS;
    }
}
