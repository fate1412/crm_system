package com.fate1412.crmSystem.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.module.customTable.dto.select.CustomTableColumnSelectDTO;
import com.fate1412.crmSystem.module.customTable.dto.select.TableColumnSelectDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomTableResultData {
    private List<CustomTableColumnSelectDTO> tableColumns;
    private List<?> tableDataList;
    private CustomTableResultData child;
    private Long thisPage;
    private Long total;
    
    public CustomTableResultData(List<CustomTableColumnSelectDTO> tableColumns, List<?> tableDataList) {
        this.tableColumns = tableColumns;
        this.tableDataList = tableDataList;
    }
    
}
