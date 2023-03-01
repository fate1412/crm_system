package com.fate1412.crmSystem.base;

import lombok.Data;

import java.util.List;

@Data
public class MyPage {
    protected List<?> records;
    protected long total;
    protected long size;
    protected long current;
    
    public MyPage(long current, long size) {
        this.size = size;
        this.current = current;
    }
}
