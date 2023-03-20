package com.fate1412.crmSystem.module.mainTable.dto.insert;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 客户
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerInsertDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 客户名称
     */
    @TableField("name")
    @TableTitle("客户名称")
    private String name;
    
    /**
     * 客户类型
     */
    @TableField("customer_type")
    @TableTitle(value = "客户类型", formType = FormType.Select)
    private Integer customerType;
    
    /**
     * 手机号
     */
    @TableField("mobile")
    @TableTitle("手机号")
    private String mobile;
    
    /**
     * 负责人
     */
    @TableField("owner")
    @TableTitle(value = "负责人", link = true, formType = FormType.Select)
    private Long owner;
    
    
}
