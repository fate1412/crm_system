package com.fate1412.crmSystem.module.customTable.controller;


import com.fate1412.crmSystem.base.MyPage;
import com.fate1412.crmSystem.base.SelectPage;
import com.fate1412.crmSystem.module.customTable.dto.insert.TableDictInsertDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableDictSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.update.TableColumnUpdateDTO;
import com.fate1412.crmSystem.module.customTable.service.ITableDictService;
import com.fate1412.crmSystem.module.mainTable.dto.select.ProductSelectDTO;
import com.fate1412.crmSystem.utils.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 数据库表字典表 前端控制器
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@RestController
@RequestMapping("/tableDict")
public class TableDictController {
    @Autowired
    private ITableDictService service;
    
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
        List<?> dtoListById = service.getDTOListById(MyCollections.toList(id));
        TableResultData tableResultData = service.getColumns();
        tableResultData.setTableDataList(dtoListById);
        return ResultTool.success(tableResultData);
    }
    
    @PreAuthorize("hasAnyAuthority('TableDict_Select')")
    @GetMapping("/getOptions")
    public JsonResult<?> getOptions(@Param("nameLike") String nameLike, @Param("page") Integer page) {
        List<IdToName> options = service.getOptions(nameLike, page);
        return ResultTool.success(options);
    }
    
    @PreAuthorize("hasAnyAuthority('TableDict_Insert')")
    @PutMapping("/add")
    public JsonResult<?> createTable(@RequestBody TableDictInsertDTO tableDictInsertDTO) {
        return service.addDTO(tableDictInsertDTO);
    }
    
    @PreAuthorize("hasAnyAuthority('TableDict_Delete')")
    @DeleteMapping("/delete")
    public JsonResult<?> delete(@RequestBody TableDictSelectDTO tableDictSelectDTO) {
        boolean b = service.delById(tableDictSelectDTO.getId());
        return ResultTool.create(b);
    }

}

