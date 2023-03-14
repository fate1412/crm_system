package com.fate1412.crmSystem.mainTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.mainTable.dto.insert.OrderProductInsertDTO;
import com.fate1412.crmSystem.mainTable.dto.select.OrderProductSelectDTO;
import com.fate1412.crmSystem.mainTable.dto.update.OrderProductUpdateDTO;
import com.fate1412.crmSystem.mainTable.service.IOrderProductService;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单产品 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@RestController
@RequestMapping("/orderProduct")
public class OrderProductController {
    @Autowired
    private IOrderProductService orderProductService;
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = orderProductService.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/page/select")
    public JsonResult<Object> selectByPage(@Param("thisPage") Long thisPage, @Param("pageSize") Long pageSize) {
        thisPage = thisPage == null ? 1 : thisPage;
        pageSize = pageSize == null ? 10 : pageSize;
        MyPage page = orderProductService.listByPage(thisPage, pageSize,null);
        TableResultData tableResultData = orderProductService.getColumns();
        tableResultData.setTableDataList(page.getRecords());
        tableResultData.setThisPage(thisPage);
        tableResultData.setTotal(page.getTotal());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('OrderProduct_Insert')")
    @PutMapping("/add")
    public JsonResult<?> add(@RequestBody OrderProductInsertDTO orderProductInsertDTO) {
        return orderProductService.addDTO(orderProductInsertDTO);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> orderProductSelectDTOList = orderProductService.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = orderProductService.getColumns();
        tableResultData.setTableDataList(orderProductSelectDTOList);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('OrderProduct_Edit')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody OrderProductUpdateDTO orderProductUpdateDTO) {
        return orderProductService.updateByDTO(orderProductUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('OrderProduct_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody OrderProductSelectDTO orderProductSelectDTO) {
        boolean b = orderProductService.delById(orderProductSelectDTO.getId());
        return ResultTool.create(b);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = orderProductService.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
}

