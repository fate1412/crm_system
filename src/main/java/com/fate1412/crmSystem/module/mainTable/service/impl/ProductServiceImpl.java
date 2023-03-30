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
import com.fate1412.crmSystem.module.mainTable.dto.insert.ProductInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.ProductSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.ProductUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.pojo.Invoice;
import com.fate1412.crmSystem.module.mainTable.pojo.Product;
import com.fate1412.crmSystem.module.mainTable.mapper.ProductMapper;
import com.fate1412.crmSystem.module.mainTable.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    @Autowired
    private ISysFlowSessionService flowSessionService;
    
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
    
        //审批
        List<Long> productIds = MyCollections.objects2List(productList, Product::getId);
        Map<Long, Integer> passMap = flowSessionService.getPass(TableNames.product, productIds);
    
        List<ProductSelectDTO> productSelectDTOList = MyCollections.copyListProperties(productList, ProductSelectDTO::new);
        productSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updater = dto.getUpdater();
            dto.setCreaterR(new IdToName(createId,userMap.get(createId), TableNames.sysUser));
            dto.setUpdaterR(new IdToName(updater,userMap.get(updater),TableNames.sysUser));
            Integer pass = passMap.get(dto.getId());
            switch (pass) {
                case 0: dto.setPass("未审批");break;
                case 1: dto.setPass("已通过");break;
                default: dto.setPass("已拒绝");
            }
        });
        return productSelectDTOList;
    }
    
    @Override
    public BaseMapper<Product> mapper() {
        return productMapper;
    }
    
    @Override
    @Transactional
    public JsonResult<?> updateByDTO(ProductUpdateDTO productUpdateDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productUpdateDTO,product);
        return update(new MyEntity<Product>(product) {
            @Override
            public Product set(Product product) {
                SysUser sysUser = sysUserService.thisUser();
                Product product2 = getById(productUpdateDTO.getId());
                product
                        .setIsShelf()
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
    public JsonResult<?> addDTO(ProductInsertDTO productInsertDTO) {
        Product product = new Product();
        BeanUtils.copyProperties(productInsertDTO,product);
        return add(new MyEntity<Product>(product) {
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
                flowSessionService.addFlowSession(TableNames.product, product.getId());
                return isRight(product);
            }
        });
    }
    
    @Override
    public MyPage listByPage(SelectPage<ProductSelectDTO> selectPage) {
        ProductSelectDTO like = selectPage.getLike();
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(like.getId() != null, Product::getId,like.getId())
                .like(like.getName() != null, Product::getName,like.getName());
        return listByPage(selectPage.getPage(),selectPage.getPageSize(),queryWrapper);
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
                .like(Product::getName,nameLike.trim());
        IPage<Product> iPage = new Page<>(page,10);
        productMapper.selectPage(iPage,queryWrapper);
        return IdToName.createList(iPage.getRecords(), Product::getId, Product::getName);
    }
    
    private ResultCode isRight(Product product) {
        //产品名称
        if (StringUtils.isBlank(product.getName())) {
            return ResultCode.PARAM_IS_BLANK;
        }
        product.setName(product.getName().trim());
        //单价
        if (product.getPrice() <= 0) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //真实库存
        if (product.getRealStock() == null) {
            return ResultCode.PARAM_IS_BLANK;
        }
        if (product.getRealStock() < 0) {
            return ResultCode.PARAM_NOT_VALID;
        }
        //库存
        if (product.getStock() < 0 || product.getStock() > product.getRealStock()){
            return ResultCode.PARAM_NOT_VALID;
        }
        //上下架时间
        if (product.getOnShelfTime().after(product.getOffShelfTime())) {
            return ResultCode.PARAM_NOT_VALID;
        }
        return ResultCode.SUCCESS;
    }
}
