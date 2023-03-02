package com.fate1412.crmSystem.mainTable.service;

import com.fate1412.crmSystem.base.MyBaseService;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductUpdateDTO;
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
    
    JsonResult<?> updateById(InvoiceProductUpdateDTO invoiceProductUpdateDTO);
    
    JsonResult<?> add(InvoiceProductSelectDTO invoiceProductSelectDTO);
}
