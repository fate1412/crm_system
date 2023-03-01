package com.fate1412.crmSystem.mainTable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.InvoiceProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 发货单产品 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IInvoiceProductService extends IService<InvoiceProduct> {
    IPage<InvoiceProductSelectDTO> listByPage(long thisPage, long pageSize);
    
    List<InvoiceProductSelectDTO> getDTOListById(List<Long> ids);
    
    boolean updateById(InvoiceProductUpdateDTO invoiceProductUpdateDTO);
    
    boolean add(InvoiceProductSelectDTO invoiceProductSelectDTO);
}
