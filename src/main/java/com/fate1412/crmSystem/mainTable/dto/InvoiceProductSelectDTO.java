package com.fate1412.crmSystem.mainTable.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
public class InvoiceProductSelectDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableField("id")
    @TableTitle(value = "发货单产品id",fixed = true,disabled = true)
    private Long id;

    /**
     * 发货单id
     */
    @TableField("invoice_id")
    private Long invoiceId;
    
    @TableTitle(value = "发货单id",link = true,disabled = true)
    private IdToName invoiceIdR;

    /**
     * 产品id
     */
    @TableField("product_id")
    private Long productId;
    
    @TableTitle(value = "产品id",link = true,disabled = true)
    private IdToName productR;

    /**
     * 发货数量
     */
    @TableField("invoice_num")
    @TableTitle("发货数量")
    private Integer invoiceNum;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @TableTitle(value = "创建时间",formType = FormType.DateTime, disabled = true)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @TableTitle(value = "更新时间",formType = FormType.DateTime, disabled = true)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    @TableTitle(value = "创建人",link = true,disabled = true)
    private Long creater;
    
    private IdToName createrR;

    /**
     * 修改人
     */
    @TableField("updater")
    @TableTitle(value = "修改人",link = true,disabled = true)
    private Long updater;
    
    private IdToName updaterR;
}
