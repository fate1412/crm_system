package com.fate1412.crmSystem.module.customTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.dto.child.TableColumnChild;
import com.fate1412.crmSystem.module.customTable.dto.insert.TableColumnInsertDTO;
import com.fate1412.crmSystem.module.customTable.dto.insert.TableDictInsertDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableOptionSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.update.TableColumnUpdateDTO;
import com.fate1412.crmSystem.module.customTable.service.ITableColumnDictService;
import com.fate1412.crmSystem.module.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.module.mainTable.dto.child.SalesOrderChild;
import com.fate1412.crmSystem.module.mainTable.dto.select.OrderProductSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.select.SalesOrderSelectDTO;
import com.fate1412.crmSystem.module.mainTable.dto.update.SalesOrderUpdateDTO;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.ResultTool;
import com.fate1412.crmSystem.utils.TableResultData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 字段字典表 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/columns")
public class TableColumnDictController {
    @Autowired
    private ITableColumnDictService service;
    @Autowired
    private ITableOptionService tableOptionService;
    
    @PreAuthorize("hasAnyAuthority('TableDict_Select')")
    @GetMapping("/getColumns")
    public JsonResult<Object> getColumns() {
        TableResultData tableResultData = service.getColumns();
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('TableDict_Select')")
    @PostMapping("/page/select")
    public JsonResult<Object> selectByPage(@RequestBody SelectPage<TableDictSelectDTO> selectPage) {
        MyPage page = service.listByPage(selectPage);
        TableResultData tableResultData = service.getColumns();
        tableResultData.setTableDataList(page.getRecords());
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('TableDict_Select')")
    @GetMapping("/select")
    public JsonResult<Object> select(@Param("id") Long id) {
        List<?> dtoList = service.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = service.getColumns();
        tableResultData.setTableDataList(dtoList);
        if (!MyCollections.isEmpty(dtoList)) {
            TableColumnSelectDTO dto = (TableColumnSelectDTO) dtoList.get(0);
            if (dto.getColumnType() == 1 && !dto.getLink()) {
                List<TableOptionSelectDTO> TableOptionSelectDTOList = tableOptionService.getDTOByTableColumnId(dto.getId());
                List<TableColumnChild> childList = MyCollections.copyListProperties(TableOptionSelectDTOList, TableColumnChild::new);
                TableResultData child = tableOptionService.getColumns(new TableColumnChild());
                if (!MyCollections.isEmpty(childList)) {
                    child.setTableDataList(childList);
                }
                tableResultData.setChild(child);
            }
        }
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('Columns_Insert')")
    @PutMapping("/add")
    public JsonResult<?> insert(@RequestBody TableColumnInsertDTO tableColumnInsertDTO) {
        return service.addDTO(tableColumnInsertDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('Columns_Insert')")
    @PostMapping("/update")
    public JsonResult<?> update(@RequestBody TableColumnUpdateDTO tableColumnUpdateDTO) {
        return service.updateByDTO(tableColumnUpdateDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('Columns_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody TableColumnSelectDTO tableColumnSelectDTO) {
        boolean b = service.delById(tableColumnSelectDTO.getId());
        return ResultTool.create(b);
    }
}

