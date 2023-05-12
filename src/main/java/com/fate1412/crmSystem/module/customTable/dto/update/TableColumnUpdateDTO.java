package com.fate1412.crmSystem.module.customTable.dto.update;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.module.customTable.dto.child.TableColumnChild;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 字段字典表
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TableColumnUpdateDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    @TableField("show_name")
    @TableTitle(value = "字段名称",inserted = true)
    private String showName;
    
    @TableField("column_index")
    @TableTitle(value = "展示顺序",inserted = true)
    private Integer columnIndex;
    
    @TableField("table_name")
    private String tableName;
    
    @TableField("inserted")
    @TableTitle(value = "新建可填写",formType = FormType.Boolean,inserted = true)
    private Boolean inserted;

    @TableField("disabled")
    @TableTitle(value = "不可修改",formType = FormType.Boolean,inserted = true)
    private Boolean disabled;
    
    private List<TableColumnChild> childList;

}
