package com.fate1412.crmSystem.moduel.mainTable.dto.update;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class CustomerUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @TableTitle(value = "客户ID",fixed = true,disabled = true)
    private Long id;

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
    @TableTitle(value = "客户类型",formType = FormType.Select)
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
    private Long owner;



}
