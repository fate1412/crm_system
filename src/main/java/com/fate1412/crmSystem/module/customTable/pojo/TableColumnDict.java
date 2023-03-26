package com.fate1412.crmSystem.module.customTable.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@TableName("table_column_dict")
public class TableColumnDict implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("table_name")
    private String tableName;

    @TableField("column_name")
    private String columnName;

    @TableField("real_column_name")
    private String realColumnName;

    @TableField("column_type")
    private Integer columnType;

    @TableField("fixed")
    private Boolean fixed;

    @TableField("disabled")
    private Boolean disabled;

    @TableField("link")
    private Boolean link;

    @TableField("show_name")
    private String showName;

    @TableField("column_index")
    private Integer columnIndex;
    
    @TableField(value = "link_table",updateStrategy = FieldStrategy.IGNORED)
    private Long linkTable;
    
    @TableField("custom")
    private Boolean custom;

    
    public static TableColumnDict create(String tableName, String columnName, String realColumnName, String showName, Integer index) {
        TableColumnDict tableColumnDict = new TableColumnDict();
        tableColumnDict
                .setTableName(tableName)
                .setColumnName(columnName)
                .setRealColumnName(realColumnName)
                .setColumnIndex(index)
                .setShowName(showName)
                .setColumnType(0)
                .setFixed(false)
                .setDisabled(false)
                .setLink(false)
                .setCustom(true);
        return tableColumnDict;
    }

}
