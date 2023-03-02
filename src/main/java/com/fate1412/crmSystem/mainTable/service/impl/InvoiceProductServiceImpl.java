package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.mapper.InvoiceMapper;
import com.fate1412.crmSystem.mainTable.mapper.ProductMapper;
import com.fate1412.crmSystem.mainTable.pojo.InvoiceProduct;
import com.fate1412.crmSystem.mainTable.mapper.InvoiceProductMapper;
import com.fate1412.crmSystem.mainTable.pojo.Product;
import com.fate1412.crmSystem.mainTable.service.IInvoiceProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultCode;
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
    
    @Override
    public JsonResult<?> updateById(InvoiceProductUpdateDTO invoiceProductUpdateDTO) {
        return updateByDTO(invoiceProductUpdateDTO, new MyEntity<InvoiceProduct>(new InvoiceProduct()) {
            @Override
            public InvoiceProduct set(InvoiceProduct invoiceProduct) {
                invoiceProduct
                        .setUpdateTime(new Date());
//                .setUpdateMember(sysUser.getUserId());
                return invoiceProduct;
            }
    
            @Override
            public ResultCode verification(InvoiceProduct invoiceProduct) {
                return ResultCode.SUCCESS;
            }
        });
    }
    
    @Override
    public JsonResult<?> add(InvoiceProductSelectDTO invoiceProductSelectDTO) {
        return add(invoiceProductSelectDTO, new MyEntity<InvoiceProduct>(new InvoiceProduct()) {
            @Override
            public InvoiceProduct set(InvoiceProduct invoiceProduct) {
                invoiceProduct
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date());
//                .setUpdateMember(sysUser.getUserId());
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
            dto.setCreaterR(new IdToName(createId,userMap.get(createId),"sysUser"));
            dto.setUpdaterR(new IdToName(updateMemberId,userMap.get(updateMemberId),"sysUser"));
            dto.setInvoiceIdR(new IdToName(invoiceId, invoiceId.toString(),"invoice"));
            dto.setProductR(new IdToName(productId,productMap.get(productId),"product"));
        
        });
        return invoiceProductSelectDTOList;
    }
    
    @Override
    public BaseMapper<InvoiceProduct> mapper() {
        return invoiceProductMapper;
    }
}
