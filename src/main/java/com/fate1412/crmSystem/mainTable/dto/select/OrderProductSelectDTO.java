package com.fate1412.crmSystem.mainTable.dto.select;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单产品
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("order_product")
public class OrderProductSelectDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    @TableTitle(value = "订单产品id",fixed = true,disabled = true)
    private Long id;

    /**
     * 销售订单id
     */
    @TableField("sales_order_id")
    @TableTitle(value = "销售订单id",link = true,disabled = true, formType = FormType.Select,inserted = true)
    private Long salesOrderId;
    
    private IdToName salesOrderIdR =new IdToName(TableNames.salesOrder);

    /**
     * 产品id
     */
    @TableField("product_id")
    @TableTitle(value = "产品id",link = true,disabled = true, formType = FormType.Select,inserted = true)
    private Long productId;
    
    private IdToName productIdR =new IdToName(TableNames.product);
    
    /**
     * 购买数量
     */
    @TableField("product_num")
    @TableTitle(value = "购买数量",inserted = true)
    private Integer productNum;

    /**
     * 已发货数量
     */
    @TableField("invoice_num")
    @TableTitle(value = "已发货数量")
    private Integer invoiceNum;

    /**
     * 原总价
     */
    @TableField("original_prices")
    @TableTitle(value = "原总价",inserted = true)
    private Double originalPrices;

    /**
     * 折后总价
     */
    @TableField("discount_prices")
    @TableTitle(value = "折后总价",inserted = true)
    private Double discountPrices;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @TableTitle(value = "创建时间",disabled = true,formType = FormType.DateTime)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @TableTitle(value = "更新时间",disabled = true,formType = FormType.DateTime)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    @TableTitle(value = "创建人",link = true, disabled = true, formType = FormType.Select)
    private Long creater;
    
    private IdToName createrR=new IdToName(TableNames.sysUser);
    
    /**
     * 修改人
     */
    @TableField("updater")
    @TableTitle(value = "修改人",link = true, disabled = true, formType = FormType.Select)
    private Long updater;
    
    private IdToName updaterR=new IdToName(TableNames.sysUser);

}
