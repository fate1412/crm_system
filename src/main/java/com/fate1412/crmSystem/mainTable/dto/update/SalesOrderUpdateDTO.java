package com.fate1412.crmSystem.mainTable.dto.update;

import com.alibaba.fastjson.annotation.JSONField;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.mainTable.dto.child.SalesOrderChild;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class SalesOrderUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableTitle(value = "销售订单Id",fixed = true,disabled = true)
    private Long id;

    /**
     * 原价
     */
    @TableTitle("原价/元")
    private Double originalPrice;

    /**
     * 折后价格
     */
    @TableTitle("折后价格/元")
    private Double discountPrice;

    /**
     * 是否通过
     */
    @TableTitle("是否通过")
    private Boolean isPass;

    /**
     * 客户id
     */
    @TableTitle("客户id")
    private Long customerId;

    /**
     * 发货状态
     */
    @TableTitle("发货状态")
    private Integer invoiceStatus;
    
    private List<SalesOrderChild> childList;

}
