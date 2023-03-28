package com.fate1412.crmSystem.module.customTable.dto.select;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 数据库表字典表
 * </p>
 *
 * @author fate1412
 * @since 2023-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("table_dict")
public class CustomTableSelectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("table_name")
    @TableTitle(value = "tableName",inserted = true)
    private String tableName;

    @TableField("show_name")
    @TableTitle(value = "表名",inserted = true)
    private String showName;
    
}
