package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.mainTable.dto.select.InvoiceSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.InvoiceUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.Invoice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;

/**
 * <p>
 * 发货单 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IInvoiceService extends IService<Invoice>, MyBaseService<Invoice> {
    
    JsonResult<?> updateById(InvoiceUpdateDTO invoiceUpdateDTO);
    
    JsonResult<?> add(InvoiceSelectDTO invoiceSelectDTO);
}
