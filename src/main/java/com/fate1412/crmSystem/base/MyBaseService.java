package com.fate1412.crmSystem.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    
    default <D> boolean updateByDTO(D dto, MyEntity<T> entity) {
        T t = entity.get();
        BeanUtils.copyProperties(dto, t);
        t = entity.set(t);
        return mapper().updateById(t) > 0;
    }
    
    
    default <D> boolean add(D dto, MyEntity<T> entity) {
        T t = entity.get();
        BeanUtils.copyProperties(dto, t);
        t = entity.set(t);
        return mapper().updateById(t) > 0;
    }
    
    List<?> getDTOList(List<T> tList);
    
    BaseMapper<T> mapper();
    
    abstract class MyEntity<T> {
        private T t;
        
        public abstract T get();
        
        public abstract T set(T t);
    }
}
