package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.mainTable.dto.select.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.InvoiceProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.InvoiceProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fate1412.crmSystem.utils.JsonResult;

/**
 * <p>
 * 发货单产品 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IInvoiceProductService extends IService<InvoiceProduct>, MyBaseService<InvoiceProduct> {
    
    JsonResult<?> updateByDTO(InvoiceProductUpdateDTO invoiceProductUpdateDTO);
    
    JsonResult<?> addDTO(InvoiceProductSelectDTO invoiceProductSelectDTO);
    
    JsonResult<?> addEntity(InvoiceProduct invoiceProduct);
    
    JsonResult<?> updateByEntity(InvoiceProduct invoiceProduct);
}
