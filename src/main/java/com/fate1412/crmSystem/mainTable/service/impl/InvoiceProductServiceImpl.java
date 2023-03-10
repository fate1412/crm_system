package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.mainTable.dto.select.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.InvoiceProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.InvoiceMapper;
import com.fate1412.crmSystem.mainTable.mapper.ProductMapper;
import com.fate1412.crmSystem.mainTable.pojo.InvoiceProduct;
import com.fate1412.crmSystem.mainTable.mapper.InvoiceProductMapper;
import com.fate1412.crmSystem.mainTable.pojo.Product;
import com.fate1412.crmSystem.mainTable.service.IInvoiceProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private SysUserMapper sysUserMapper;
    @Autowired
    private InvoiceProductMapper invoiceProductMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ITableOptionService tableOptionService;
    @Autowired
    private ISysUserService sysUserService;
    
    @Override
    public JsonResult<?> updateByDTO(InvoiceProductUpdateDTO invoiceProductUpdateDTO) {
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        BeanUtils.copyProperties(invoiceProductUpdateDTO,invoiceProduct);
        return updateByEntity(invoiceProduct);
    }
    
    @Override
    public JsonResult<?> updateByEntity(InvoiceProduct invoiceProduct) {
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
                return ResultCode.SUCCESS;
            }
        });
    }
    
    @Override
    public JsonResult<?> addDTO(InvoiceProductSelectDTO invoiceProductSelectDTO) {
        InvoiceProduct invoiceProduct = new InvoiceProduct();
        BeanUtils.copyProperties(invoiceProductSelectDTO,invoiceProduct);
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
                return null;
            }
        });
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
        
        List<SysUser> sysUserList = sysUserMapper.selectBatchIds(userIdList);
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
    public List<IdToName> getOptions(String nameLike, Integer page) {
        QueryWrapper<InvoiceProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(InvoiceProduct::getId)
                .like(InvoiceProduct::getId, nameLike);
        IPage<InvoiceProduct> iPage = new Page<>(page, 10);
        invoiceProductMapper.selectPage(iPage, queryWrapper);
        return IdToName.createList2(iPage.getRecords(), InvoiceProduct::getId, InvoiceProduct::getId);
    }
}
