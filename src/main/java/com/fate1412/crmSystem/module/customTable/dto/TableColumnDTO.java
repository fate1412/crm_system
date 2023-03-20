package com.fate1412.crmSystem.module.customTable.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class TableColumnDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("table_name")
    private String tableName;

    @TableField("column_name")
    private String columnName;

    @TableField("real_column_name")
    private String realColumnName;

    @TableField("column_type")
    private List<OptionDTO> options = new ArrayList<>();

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
