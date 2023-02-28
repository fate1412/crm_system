package com.fate1412.crmSystem.mainTable.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class SalesOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JSONField(name = "id")
    private Integer id;

    /**
     * 原价
     */
    @JSONField(name = "original_price")
    private Double originalPrice;

    /**
     * 折后价格
     */
    @JSONField(name = "discount_price")
    private Double discountPrice;

    /**
     * 是否通过
     */
    @JSONField(name = "is_pass")
    private Integer isPass;

    /**
     * 客户id
     */
    @JSONField(name = "customer_id")
    private Integer customerId;

    /**
     * 发货状态
     */
    @JSONField(name = "invoice_status")
    private Integer invoiceStatus;

    /**
     * 创建时间
     */
    @JSONField(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JSONField(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @JSONField(name = "creater")
    private String creater;

    /**
     * 修改人
     */
    @JSONField(name = "updater")
    private String updater;

    @JSONField(name = "del_flag")
    private String delFlag;


}
