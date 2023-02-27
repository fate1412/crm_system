package com.fate1412.crmSystem.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
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
    @TableTitle("客户ID")
    private Integer id;

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
    @TableTitle("客户类型")
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
    @TableTitle("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @TableTitle("更新时间")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    @TableTitle("创建人")
    private Integer creater;

    /**
     * 更新者
     */
    @TableField("update_member")
    @TableTitle("更新者")
    private Integer updateMember;

    /**
     * 负责人
     */
    @TableField("owner")
    @TableTitle("负责人")
    private Integer owner;


}
