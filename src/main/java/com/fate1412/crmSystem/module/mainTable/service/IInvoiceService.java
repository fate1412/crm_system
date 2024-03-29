package com.fate1412.crmSystem.module.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.mainTable.dto.insert.InvoiceInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.InvoiceSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.InvoiceUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.pojo.Invoice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.TableResultData;

/**
 * <p>
 * 发货单 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IInvoiceService extends IService<Invoice>, MyBaseService<Invoice> {
    
    JsonResult<?> updateByDTO(InvoiceUpdateDTO invoiceUpdateDTO);
    
    JsonResult<?> addDTO(InvoiceInsertDTO invoiceInsertDTO);
    
    <D> TableResultData getColumns(D dto);
    
    boolean delById(Long id);
    
    /**
     * 发货
     */
    boolean invoiceById(Long id);
    
    MyPage listByPage(SelectPage<InvoiceSelectDTO> selectPage);
}
