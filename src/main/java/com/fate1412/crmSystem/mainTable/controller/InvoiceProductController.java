package com.fate1412.crmSystem.mainTable.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.InvoiceUpdateDTO;
import com.fate1412.crmSystem.mainTable.service.IInvoiceProductService;
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
    
    @PreAuthorize("permitAll()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        List<TableColumn> tableColumns = TableResultData.tableColumnList(InvoiceProductSelectDTO.class);
        return ResultTool.success(tableColumns);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 20 : pageSize;
        IPage<InvoiceProductSelectDTO> page = invoiceProductService.listByPage(thisPage, pageSize);
        TableResultData tableResultData = TableResultData.createTableResultData(page, InvoiceProductSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody InvoiceProductSelectDTO invoiceProductSelectDTO) {
        boolean b = invoiceProductService.add(invoiceProductSelectDTO);
        return ResultTool.create(b);
    }
    
    @PreAuthorize("permitAll()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<InvoiceProductSelectDTO> invoiceProductSelectDTOList = invoiceProductService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = TableResultData.createTableResultData(invoiceProductSelectDTOList, InvoiceProductSelectDTO.class);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("permitAll()")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody InvoiceProductUpdateDTO invoiceProductUpdateDTO) {
        boolean b = invoiceProductService.updateById(invoiceProductUpdateDTO);
        return ResultTool.create(b);
    }
    
    @PreAuthorize("permitAll()")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody InvoiceProductSelectDTO invoiceProductSelectDTO) {
        boolean b = invoiceProductService.removeById(invoiceProductSelectDTO.getId());
        return ResultTool.create(b);
    }
}
