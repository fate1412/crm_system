package com.fate1412.crmSystem.mainTable.service.impl;

import com.fate1412.crmSystem.mainTable.pojo.Product;
import com.fate1412.crmSystem.mainTable.mapper.ProductMapper;
import com.fate1412.crmSystem.mainTable.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
