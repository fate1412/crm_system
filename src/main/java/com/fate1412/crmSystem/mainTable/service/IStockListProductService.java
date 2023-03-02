package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.mainTable.dto.ProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.StockListProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.dto.StockListUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.StockListProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;

/**
 * <p>
 * 备货单产品 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IStockListProductService extends IService<StockListProduct>, MyBaseService<StockListProduct> {
    JsonResult<?> updateById(StockListProductUpdateDTO stockListProductUpdateDTO);
    
    JsonResult<?> add(StockListProductUpdateDTO stockListProductUpdateDTO);
}
