package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.mainTable.dto.update.ProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;

/**
 * <p>
 * 产品 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IProductService extends IService<Product>, MyBaseService<Product> {
    
    JsonResult<?> updateById(ProductUpdateDTO productUpdateDTO);
    
    JsonResult<?> add(ProductUpdateDTO productUpdateDTO);
}
