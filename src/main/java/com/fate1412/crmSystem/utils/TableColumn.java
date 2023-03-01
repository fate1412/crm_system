package com.fate1412.crmSystem.utils;

import com.fate1412.crmSystem.annotations.TableTitle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class TableColumn {
    private String title;
    private String name;
    private Boolean fixed = false;
    private Boolean link = false;
    private Boolean disabled = false;
    private TableTitle.FormType formType = TableTitle.FormType.Input;
    
    public TableColumn(String title, String name) {
        this.title = title;
        this.name = name;
    }
}
