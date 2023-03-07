package com.fate1412.crmSystem.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.customTable.dto.OptionDTO;
import com.fate1412.crmSystem.customTable.service.ITableOptionService;
import com.fate1412.crmSystem.mainTable.dto.select.CustomerSelectDTO;
import com.fate1412.crmSystem.utils.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

public interface MyBaseService<T> {
    
    
    
    default MyPage listByPage(long thisPage, long pageSize, QueryWrapper<T> queryWrapper) {
        IPage<T> page = new Page<>(thisPage, pageSize);
        mapper().selectPage(page, queryWrapper);
        List<T> tList = page.getRecords();
        List<?> dtoList = getDTOList(tList);
        MyPage myPage = new MyPage(thisPage, pageSize);
        myPage.setRecords(dtoList);
        myPage.setCurrent(page.getCurrent());
        myPage.setTotal(page.getTotal());
        return myPage;
    }
    
    default List<?> getDTOListById(List<Long> ids) {
        List<T> tList = mapper().selectBatchIds(ids);
        return getDTOList(tList);
    }
    
    default <D> JsonResult<?> updateByDTO(D dto, MyEntity<T> entity) {
        T t = entity.getT();
        BeanUtils.copyProperties(dto, t);
        t = entity.set(t);
        if (entity.verification(t).equals(ResultCode.SUCCESS)) {
            return mapper().updateById(t) > 0? ResultTool.success() : ResultTool.fail(ResultCode.UPDATE_ERROR);
        } else {
            return ResultTool.fail(entity.verification(t));
        }
    }
    
    
    default <D> JsonResult<?> add(D dto, MyEntity<T> entity) {
        T t = entity.getT();
        BeanUtils.copyProperties(dto, t);
        t = entity.set(t);
        if (entity.verification(t).equals(ResultCode.SUCCESS) && (mapper().insert(t) > 0)) {
            return entity.afterAdd()? ResultTool.success() : ResultTool.fail(ResultCode.INSERT_ERROR);
        } else {
            return ResultTool.fail(entity.verification(t));
        }
    }
    
    default <D> TableResultData getColumns(String tableName, D dto, ITableOptionService tableOptionService) {
        List<TableColumn> tableColumnList = TableResultData.tableColumnList(dto.getClass());
        tableColumnList.forEach(tableColumn -> {
            if (FormType.Select.equals(tableColumn.getFormType())) {
                List<OptionDTO> optionDTOS = tableOptionService.getOptions(tableName, tableColumn.getProp());
                tableColumn.setOptions(optionDTOS);
            }
        });
        TableResultData tableResultData = new TableResultData();
        tableResultData.setTableColumns(tableColumnList);
        tableResultData.setTableDataList(MyCollections.toList(dto));
        return tableResultData;
    }
    
    List<IdToName> getOptions(String nameLike, Integer page);
    
    TableResultData getColumns();
    
    List<?> getDTOList(List<T> tList);
    
    BaseMapper<T> mapper();
    
    @AllArgsConstructor
    @Data
    abstract class MyEntity<T> {
        private T t;
        
        public abstract T set(T t);
        
        public ResultCode verification(T t) {
            return ResultCode.SUCCESS;
        }
        
        public boolean afterAdd() {
            return true;
        }
    }
}
