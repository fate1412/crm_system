package com.fate1412.crmSystem.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
public class CustomerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @TableTitle(value = "客户ID",fixed = true)
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
    private String customerType;

    /**
     * 手机号
     */
    @TableField("mobile")
    @TableTitle("手机号")
    private String mobile;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @TableTitle(value = "创建时间",disabled = true,formType = FormType.Date)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @TableTitle(value = "更新时间",disabled = true,formType = FormType.Date)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    private Long creater;
    
    @TableTitle(value = "创建人",link = true,disabled = true)
    private IdToName createrR;

    /**
     * 更新者
     */
    @TableField("update_member")
    private Long updateMember;
    
    @TableTitle(value = "更新者",link = true,disabled = true)
    private IdToName updateMemberR;

    /**
     * 负责人
     */
    @TableField("owner")
    private Long owner;
    
    @TableTitle(value = "负责人",link = true,disabled = true)
    private IdToName ownerR;


}
