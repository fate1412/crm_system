package com.fate1412.crmSystem.customTable.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
    private String columnType;

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


}
