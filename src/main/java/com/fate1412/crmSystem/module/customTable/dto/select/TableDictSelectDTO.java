package com.fate1412.crmSystem.module.customTable.dto.select;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
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
public class TableDictSelectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    @TableField("show_name")
    @TableTitle(value = "表名称",inserted = true)
    private String showName;
    
    @TableField("table_name")
    @TableTitle(value = "tableName",inserted = true)
    private String tableName;
    
    @TableField("real_table_name")
    @NotBlank
    @TableTitle(value = "真实表名",disabled = true,inserted = true)
    private String realTableName;

    
    
    @TableField("custom")
    private Boolean custom;

}
