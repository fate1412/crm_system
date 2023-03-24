package com.fate1412.crmSystem.module.customTable.dto.child;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fate1412.crmSystem.annotations.TableTitle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 字段选择值
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TableColumnChild implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    @TableField("table_name")
    private String tableName;
    
    @TableField("column_name")
    private String columnName;

    @TableField("option_key")
    @TableTitle("key")
    private Integer optionKey;

    @TableField("option")
    @TableTitle("value")
    private String option;
    
    private final Boolean isEditor = false;
}
