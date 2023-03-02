package com.fate1412.crmSystem.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.ResultCode;
import com.fate1412.crmSystem.utils.ResultTool;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

public interface MyBaseService<T> {
    
    
    default MyPage listByPage(long thisPage, long pageSize) {
        IPage<T> page = new Page<>(thisPage, pageSize);
        mapper().selectPage(page, null);
        List<T> tList = page.getRecords();
        List<?> dtoList = getDTOList(tList);
        MyPage myPage = new MyPage(thisPage, pageSize);
        myPage.setRecords(dtoList);
        myPage.setCurrent(page.getCurrent());
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
        if (entity.verification(t).equals(ResultCode.SUCCESS)) {
            return mapper().insert(t) > 0? ResultTool.success() : ResultTool.fail(ResultCode.INSERT_ERROR);
        } else {
            return ResultTool.fail(entity.verification(t));
        }
    }
    
    List<?> getDTOList(List<T> tList);
    
    BaseMapper<T> mapper();
    
    @AllArgsConstructor
    @Data
    abstract class MyEntity<T> {
        private T t;
        
        public abstract T set(T t);
        
        public abstract ResultCode verification(T t);
    }
}
