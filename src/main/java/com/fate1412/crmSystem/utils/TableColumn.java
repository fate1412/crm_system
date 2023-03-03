package com.fate1412.crmSystem.utils;

import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class TableColumn {
    private String title;
    private String name;
    private Boolean fixed = false;
    private Boolean link = false;
    private Boolean disabled = false;
    private FormType formType = FormType.Input;
    private List<Map<Integer,String>> options;
    
    public TableColumn(String title, String name) {
        this.title = title;
        this.name = name;
    }
}
