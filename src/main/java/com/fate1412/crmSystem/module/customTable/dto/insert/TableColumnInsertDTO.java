package com.fate1412.crmSystem.module.customTable.dto.insert;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class TableColumnInsertDTO {

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
    
    @TableField("real_column_name")
    private String realColumnName;

    @TableField("column_name")
    @TableTitle(value = "columnName",inserted = true)
    private String columnName;

    @TableField("column_type")
    @TableTitle(value = "数据类型",formType = FormType.Select, inserted = true)
    private Integer columnType;
    
    private String columnTypeR;

    @TableField("disabled")
    @TableTitle(value = "不可修改",formType = FormType.Boolean,inserted = true)
    private Boolean disabled;
    
    @TableField("inserted")
    @TableTitle(value = "新建可填写",formType = FormType.Boolean,inserted = true)
    private Boolean inserted;

    @TableField("link")
    @TableTitle(value = "可链接",formType = FormType.Boolean,inserted = true)
    private Boolean link;
    
    @TableField(value = "link_table",updateStrategy = FieldStrategy.IGNORED)
    private Long linkTable;

}
