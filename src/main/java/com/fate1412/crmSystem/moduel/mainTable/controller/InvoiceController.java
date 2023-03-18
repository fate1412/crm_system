package com.fate1412.crmSystem.moduel.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.moduel.mainTable.dto.child.InvoiceChild;
import com.fate1412.crmSystem.moduel.mainTable.dto.insert.InvoiceInsertDTO;
import com.fate1412.crmSystem.moduel.mainTable.dto.select.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.moduel.mainTable.dto.select.InvoiceSelectDTO;
import com.fate1412.crmSystem.moduel.mainTable.dto.update.InvoiceUpdateDTO;
import com.fate1412.crmSystem.moduel.mainTable.service.IInvoiceProductService;
import com.fate1412.crmSystem.moduel.mainTable.service.IInvoiceService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 发货单 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    private IInvoiceService invoiceService;
    @Autowired
    private IInvoiceProductService invoiceProductService;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = invoiceService.getColumns();
        tableResultData.setChild(invoiceService.getColumns(new InvoiceChild()));
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<InvoiceSelectDTO> selectPage) {
        MyPage page = invoiceService.listByPage(selectPage);
        TableResultData tableResultData = invoiceService.getColumns();
        tableResultData.setTableDataList(page.getRecords());
        tableResultData.setThisPage(selectPage.getPage());
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('Invoice_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody InvoiceInsertDTO invoiceInsertDTO) {
        return invoiceService.addDTO(invoiceInsertDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoList = invoiceService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = invoiceService.getColumns();
        tableResultData.setTableDataList(dtoList);
        if (MyCollections.isEmpty(dtoList)) {
            tableResultData.setChild(new TableResultData());
        } else {
            InvoiceSelectDTO dto = (InvoiceSelectDTO) dtoList.get(0);
            List<InvoiceProductSelectDTO> invoiceProductSelectDTOS = invoiceProductService.getDTOByInvoiceId(dto.getId());
            List<InvoiceChild> childList = MyCollections.copyListProperties(invoiceProductSelectDTOS, InvoiceChild::new);
            TableResultData child = invoiceProductService.getColumns(new InvoiceChild());
            if(!MyCollections.isEmpty(childList)) {
                child.setTableDataList(childList);
            }
            tableResultData.setChild(child);
        }
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('Invoice_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody InvoiceUpdateDTO invoiceUpdateDTO) {
        return invoiceService.updateByDTO(invoiceUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('Invoice_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody InvoiceSelectDTO invoiceSelectDTO) {
        boolean b = invoiceService.delById(invoiceSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = invoiceService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
}

