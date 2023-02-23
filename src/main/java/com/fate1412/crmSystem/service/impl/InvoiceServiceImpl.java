package com.fate1412.crmSystem.service.impl;

import com.fate1412.crmSystem.pojo.Invoice;
import com.fate1412.crmSystem.mapper.InvoiceMapper;
import com.fate1412.crmSystem.service.IInvoiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 发货单 服务实现类
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements IInvoiceService {

}
