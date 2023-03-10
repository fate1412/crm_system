package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.mainTable.dto.insert.StockListProductInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.update.StockListProductUpdateDTO;
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
    JsonResult<?> updateByDTO(StockListProductUpdateDTO stockListProductUpdateDTO);
    
    JsonResult<?> addDTO(StockListProductInsertDTO stockListProductInsertDTO);
}
