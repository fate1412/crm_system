package com.fate1412.crmSystem.module.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.mainTable.dto.child.SalesOrderChild;
import com.fate1412.crmSystem.module.mainTable.dto.insert.SalesOrderInsertDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.OrderProductSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.SalesOrderSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.SalesOrderUpdateDTO;
import com.fate1412.crmSystem.module.mainTable.service.IOrderProductService;
import com.fate1412.crmSystem.module.mainTable.service.ISalesOrderService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 销售订单 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/salesOrder")
public class SalesOrderController {
    @Autowired
    private ISalesOrderService salesOrderService;
    @Autowired
    private IOrderProductService orderProductService;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = salesOrderService.getColumns();
        tableResultData.setChild(orderProductService.getColumns(new SalesOrderChild()));
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<SalesOrderSelectDTO> selectPage) {
        MyPage page = salesOrderService.listByPage(selectPage);
        TableResultData tableResultData = salesOrderService.getColumns();
        tableResultData.setTableDataList(page.getRecords());
        tableResultData.setThisPage(selectPage.getPage());
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('SalesOrder_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody SalesOrderInsertDTO salesOrderInsertDTO) {
        return salesOrderService.addDTO(salesOrderInsertDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoList = salesOrderService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = salesOrderService.getColumns();
        tableResultData.setTableDataList(dtoList);
        if (MyCollections.isEmpty(dtoList)) {
            return ResultTool.fail(ResultCode.DATA_NOT_FOUND);
        } else {
            SalesOrderSelectDTO dto = (SalesOrderSelectDTO) dtoList.get(0);
            List<OrderProductSelectDTO> orderProductSelectDTOS = orderProductService.getDTOBySalesOrderId(dto.getId());
            List<SalesOrderChild> childList = MyCollections.copyListProperties(orderProductSelectDTOS, SalesOrderChild::new);
            TableResultData child = orderProductService.getColumns(new SalesOrderChild());
            if(!MyCollections.isEmpty(childList)) {
                child.setTableDataList(childList);
            }
            tableResultData.setChild(child);
        }
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('SalesOrder_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody SalesOrderUpdateDTO salesOrderUpdateDTO) {
        return salesOrderService.updateByDTO(salesOrderUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('SalesOrder_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody SalesOrderSelectDTO salesOrderSelectDTO) {
        boolean b = salesOrderService.delById(salesOrderSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = salesOrderService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
}

