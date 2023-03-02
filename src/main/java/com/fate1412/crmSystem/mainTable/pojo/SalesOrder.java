package com.fate1412.crmSystem.mainTable.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 销售订单
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sales_order")
public class SalesOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 原价
     */
    @TableField("original_price")
    private Double originalPrice;

    /**
     * 折后价格
     */
    @TableField("discount_price")
    private Double discountPrice;

    /**
     * 是否通过
     */
    @TableField("is_pass")
    private Boolean isPass;

    /**
     * 客户id
     */
    @TableField("customer_id")
    private Long customerId;

    /**
     * 发货状态
     */
    @TableField("invoice_status")
    private Integer invoiceStatus;

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
     * 修改人
     */
    @TableField("updater")
    private Long updater;

    @TableField("del_flag")
    @TableLogic
    private Boolean delFlag;


}
