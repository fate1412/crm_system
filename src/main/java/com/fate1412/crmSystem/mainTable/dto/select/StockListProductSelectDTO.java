package com.fate1412.crmSystem.mainTable.dto.select;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 备货单产品
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stock_list_product")
@Accessors(chain = true)
public class StockListProductSelectDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId("id")
    @TableTitle(value = "备货单产品Id", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 备货单id
     */
    @TableField("stock_list_id")
    @TableTitle(value = "备货单Id", link = true, disabled = true, formType = FormType.Select,inserted = true)
    private Long stockListId;
    
    private IdToName stockListIdR = new IdToName(TableNames.stockList);
    
    /**
     * 产品id
     */
    @TableField("product_id")
    @TableTitle(value = "产品id", link = true, disabled = true, formType = FormType.Select,inserted = true)
    private Long productId;
    
    private IdToName productIdR = new IdToName(TableNames.product);
    
    /**
     * 备货数量
     */
    @TableField("stock_num")
    @TableTitle(value = "备货数量",inserted = true)
    private Integer stockNum;
    
    /**
     * 价格
     */
    @TableField("price")
    @TableTitle(value = "价格",inserted = true)
    private Double price;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    @TableTitle(value = "创建时间", disabled = true, formType = FormType.DateTime)
    private Date createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    @TableTitle(value = "更新时间", disabled = true, formType = FormType.DateTime)
    private Date updateTime;
    
    /**
     * 创建人
     */
    @TableField("creater")
    @TableTitle(value = "创建人", link = true, disabled = true, formType = FormType.Select)
    private Long creater;
    
    private IdToName createrR = new IdToName(TableNames.sysUser);
    
    /**
     * 修改人
     */
    @TableField("updater")
    @TableTitle(value = "修改人", link = true, disabled = true, formType = FormType.Select)
    private Long updater;
    
    private IdToName updaterR = new IdToName(TableNames.sysUser);
    
    
}
