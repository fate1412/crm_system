package com.fate1412.crmSystem.module.customTable.dto.select;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
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
public class TableColumnSelectDTO {

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

    @TableField("column_name")
    @TableTitle(value = "columnName",inserted = true)
    private String columnName;

    @TableField("column_type")
    @TableTitle(value = "数据类型",formType = FormType.Select, inserted = true)
    private Integer columnType;
    
    private String columnTypeR;

    @TableField("disabled")
    @TableTitle(value = "不可修改",formType = FormType.Switch,inserted = true)
    private Boolean disabled;
    
    @TableField("fixed")
    private Boolean fixed;

    @TableField("link")
    @TableTitle(value = "可链接(非主键)",formType = FormType.Switch,inserted = true)
    private Boolean link;
    
    @TableField("custom")
    private Boolean custom;

}
