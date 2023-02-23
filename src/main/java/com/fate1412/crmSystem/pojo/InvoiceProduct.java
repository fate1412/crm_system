package com.fate1412.crmSystem.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class InvoiceProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发货单id
     */
    @TableField("invoice_id")
    private Integer invoiceId;

    /**
     * 产品id
     */
    @TableField("product_id")
    private Integer productId;

    /**
     * 发货数量
     */
    @TableField("invoice_num")
    private Integer invoiceNum;

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
    private String creater;

    /**
     * 修改人
     */
    @TableField("updater")
    private String updater;

    @TableField("del_flag")
    @TableLogic
    private String delFlag;


}
