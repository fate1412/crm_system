package com.fate1412.crmSystem.moduel.mainTable.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 发货单产品
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("invoice_product")
@Accessors(chain = true)
public class InvoiceProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableField("id")
    private Long id;

    /**
     * 发货单id
     */
    @TableField("invoice_id")
    private Long invoiceId;

    /**
     * 产品id
     */
    @TableField("product_id")
    private Long productId;

    /**
     * 发货数量
     */
    @TableField("invoice_num")
    private Integer invoiceNum;

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

}
