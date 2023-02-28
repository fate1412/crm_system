package com.fate1412.crmSystem.mainTable.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("customer")
@Accessors(chain = true)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 客户名称
     */
    @TableField("name")
    private String name;

    /**
     * 客户类型
     */
    @TableField("customer_type")
    private String customerType;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    private Long creater;

    /**
     * 更新者
     */
    @TableField("update_member")
    private Long updateMember;

    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;

    /**
     * 负责人
     */
    @TableField("owner")
    private Long owner;


}
