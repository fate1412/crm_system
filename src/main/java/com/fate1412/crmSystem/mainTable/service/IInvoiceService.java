package com.fate1412.crmSystem.mainTable.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate1412.crmSystem.mainTable.dto.InvoiceSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceUpdateDTO;
import com.fate1412.crmSystem.mainTable.pojo.Invoice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 发货单 服务类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
public interface IInvoiceService extends IService<Invoice> {
    IPage<InvoiceSelectDTO> listByPage(long thisPage, long pageSize);
    
    List<InvoiceSelectDTO> getDTOListById(List<Long> ids);
    
    boolean updateById(InvoiceUpdateDTO invoiceUpdateDTO);
    
    boolean add(InvoiceSelectDTO invoiceSelectDTO);
}
