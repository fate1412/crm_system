package com.fate1412.crmSystem.mainTable.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fate1412.crmSystem.mainTable.dto.OrderProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.ProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.ProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.OrderProduct;
import com.fate1412.crmSystem.mainTable.pojo.Product;
import com.fate1412.crmSystem.mainTable.mapper.ProductMapper;
import com.fate1412.crmSystem.mainTable.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.utils.IdToName;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private SysUserMapper sysUserMapper;
    
    @Override
    public List<?> getDTOList(List<Product> productList) {
        if (MyCollections.isEmpty(productList)) {
            return new ArrayList<>();
        }
        //员工
        List<Long> createIds = MyCollections.objects2List(productList, Product::getCreater);
        List<Long> updateMemberIds = MyCollections.objects2List(productList, Product::getUpdater);
        List<Long> userIdList = MyCollections.addList(true, createIds, updateMemberIds);
    
        List<SysUser> sysUserList = sysUserMapper.selectBatchIds(userIdList);
        Map<Long, String> userMap = MyCollections.list2MapL(sysUserList, SysUser::getUserId, SysUser::getRealName);
    
        List<ProductSelectDTO> productSelectDTOList = MyCollections.copyListProperties(productList, ProductSelectDTO::new);
        productSelectDTOList.forEach(dto -> {
            Long createId = dto.getCreater();
            Long updater = dto.getUpdater();
            dto.setCreaterR(new IdToName(createId,userMap.get(createId),"sysUser"));
            dto.setUpdaterR(new IdToName(updater,userMap.get(updater),"sysUser"));
        });
        return productSelectDTOList;
    }
    
    @Override
    public BaseMapper<Product> mapper() {
        return productMapper;
    }
    
    @Override
    public JsonResult<?> updateById(ProductUpdateDTO productUpdateDTO) {
        return updateByDTO(productUpdateDTO, new MyEntity<Product>(new Product()) {
            @Override
            public Product set(Product product) {
                product
                        .setUpdateTime(new Date());
                return product;
            }
        });
    }
    
    @Override
    public JsonResult<?> add(ProductUpdateDTO productUpdateDTO) {
        return add(productUpdateDTO, new MyEntity<Product>(new Product()) {
            @Override
            public Product set(Product product) {
                product
                        .setCreateTime(new Date());
                return product;
            }
        });
    }
}
