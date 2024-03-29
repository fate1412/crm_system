package com.fate1412.crmSystem.module.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.mainTable.dto.insert.StockListInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.StockListSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.StockListUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.pojo.StockList;
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
    
    MyPage listByPage(SelectPage<StockListSelectDTO> selectPage);
    
    boolean stockUp(Long id);
}
