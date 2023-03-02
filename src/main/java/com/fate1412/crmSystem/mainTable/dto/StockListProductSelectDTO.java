package com.fate1412.crmSystem.mainTable.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
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
    @TableTitle(value = "备货单产品Id",fixed = true,disabled = true)
    private Long id;

    /**
     * 备货单id
     */
    @TableField("stock_list_id")
    private Long stockListId;
    
    @TableTitle(value = "备货单Id",link = true,disabled = true)
    private IdToName stockListR;

    /**
     * 产品id
     */
    @TableField("product_id")
    private Long productId;
    
    @TableTitle(value = "产品id",link = true,disabled = true)
    private IdToName productR;

    /**
     * 备货数量
     */
    @TableField("stock_num")
    @TableTitle("备货数量")
    private Integer stockNum;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @TableTitle(value = "创建时间",disabled = true,formType = TableTitle.FormType.DateTime)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @TableTitle(value = "更新时间",disabled = true,formType = TableTitle.FormType.DateTime)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField("creater")
    private Long creater;
    
    @TableTitle(value = "创建人",link = true,disabled = true)
    private IdToName createrR;

    /**
     * 修改人
     */
    @TableField("updater")
    private Long updater;
    
    @TableTitle(value = "修改人",link = true,disabled = true)
    private IdToName updaterR;


}
