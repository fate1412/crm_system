package com.fate1412.crmSystem.moduel.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.moduel.mainTable.dto.select.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.moduel.mainTable.dto.update.InvoiceProductUpdateDTO;
import com.fate1412.crmSystem.moduel.mainTable.service.IInvoiceProductService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 发货单产品 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/invoiceProduct")
public class InvoiceProductController {
    @Autowired
    private IInvoiceProductService invoiceProductService;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = invoiceProductService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<InvoiceProductSelectDTO> selectPage) {
        MyPage page = invoiceProductService.listByPage(selectPage);
        TableResultData tableResultData = invoiceProductService.getColumns();
        tableResultData.setTableDataList(page.getRecords());
        tableResultData.setThisPage(selectPage.getPage());
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('InvoiceProduct_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody InvoiceProductSelectDTO invoiceProductSelectDTO) {
        return invoiceProductService.addDTO(invoiceProductSelectDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> invoiceProductSelectDTOList = invoiceProductService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = invoiceProductService.getColumns();
        tableResultData.setTableDataList(invoiceProductSelectDTOList);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('InvoiceProduct_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody InvoiceProductUpdateDTO invoiceProductUpdateDTO) {
        return invoiceProductService.updateByDTO(invoiceProductUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('InvoiceProduct_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody InvoiceProductSelectDTO invoiceProductSelectDTO) {
        boolean b = invoiceProductService.removeById(invoiceProductSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = invoiceProductService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
}

