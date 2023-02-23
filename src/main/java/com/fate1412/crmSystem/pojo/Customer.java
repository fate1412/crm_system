package com.fate1412.crmSystem.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    private Integer creater;

    /**
     * 更新者
     */
    @TableField("update_manber")
    private Integer updateManber;

    @TableField("del_flag")
    @TableLogic
    private String delFlag;

    /**
     * 负责人
     */
    @TableField("owner")
    private Integer owner;


}
