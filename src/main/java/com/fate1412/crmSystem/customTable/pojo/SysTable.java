package com.fate1412.crmSystem.customTable.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 表/字段名字典表
 * </p>
 *
 * @author fate1412
 * @since 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_table")
public class SysTable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表名
     */
    @TableId("table_name")
    private String tableName;

    /**
     * 字段名
     */
    @TableField("table_column")
    private String tableColumn;

    @TableField("name")
    private String name;

    @TableField("column_name")
    private String columnName;

    /**
     * 字段展示名
     */
    @TableField("column_name_r")
    private String columnNameR;

    /**
     * 是否固定表头
     */
    @TableField("fixed")
    private Boolean fixed;

    /**
     * 是否可点击
     */
    @TableField("link")
    private Boolean link;

    /**
     * 是否不可编辑
     */
    @TableField("disabled")
    private Boolean disabled;

    /**
     * 表格字段类别
     */
    @TableField("form_type")
    private String formType;


}
