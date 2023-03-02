package com.fate1412.crmSystem.mainTable.dto;

import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
public class SalesOrderSelectDTO implements Serializable {

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
    @TableTitle(value = "客户id",link = true,disabled = true)
    private IdToName customerR;
    
    private Long customerId;

    /**
     * 发货状态
     */
    @TableTitle("发货状态")
    private Integer invoiceStatus;

    /**
     * 创建时间
     */
    @TableTitle(value = "创建时间",disabled = true,fixed = true)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableTitle(value = "更新时间",disabled = true,fixed = true)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableTitle(value = "创建人",link = true,disabled = true)
    private IdToName createrR;
    
    private Long creater;

    /**
     * 修改人
     */
    @TableTitle(value = "修改人",link = true,disabled = true)
    private IdToName updaterR;
    
    private Long updater;


}
