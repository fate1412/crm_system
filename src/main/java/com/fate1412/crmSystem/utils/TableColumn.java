package com.fate1412.crmSystem.utils;

import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.customTable.dto.OptionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class TableColumn {
    private String label;
    private String prop;
    private Boolean fixed = false;
    private Boolean link = false;
    private Boolean disabled = false;
    private Boolean inserted = true;
    private FormType formType = FormType.Input;
    private List<OptionDTO> options;
    
    public TableColumn(String label, String prop) {
        this.label = label;
        this.prop = prop;
    }
}
