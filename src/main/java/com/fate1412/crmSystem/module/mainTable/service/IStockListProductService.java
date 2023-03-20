package com.fate1412.crmSystem.module.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.mainTable.dto.insert.StockListProductInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.StockListProductSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.StockListProductUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.pojo.StockListProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.TableResultData;

import java.util.List;

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
    
    JsonResult<?> updateByEntity(StockListProduct stockListProduct);
    
    JsonResult<?> addEntity(StockListProduct stockListProduct);
    
    List<StockListProductSelectDTO> getDTOByStockListId(Long stockListId);
    
    <D> TableResultData getColumns(D dto);
    
    boolean delById(Long id);
    
    boolean delByIds(List<Long> ids);
    
    boolean delByStockListId(Long id);
    
    MyPage listByPage(SelectPage<StockListProductSelectDTO> selectPage);
}
