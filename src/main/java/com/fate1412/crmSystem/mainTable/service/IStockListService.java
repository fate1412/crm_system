package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.mainTable.dto.insert.StockListInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.update.StockListUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.StockList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;

/**
 * <p>
 * 备货单 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IStockListService extends IService<StockList>, MyBaseService<StockList> {
    JsonResult<?> updateByDTO(StockListUpdateDTO stockListUpdateDTO);
    
    JsonResult<?> addDTO(StockListInsertDTO stockListInsertDTO);
    
    boolean delById(Long id);
}
