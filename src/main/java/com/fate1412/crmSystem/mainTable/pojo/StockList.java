package com.fate1412.crmSystem.mainTable.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 备货单
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("stock_list")
@Accessors(chain = true)
public class StockList implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 备货日期
     */
    @TableField("stock_up_date")
    private Date stockUpDate;

    /**
     * 总价
     */
    @TableField("prices")
    private Double prices;

    /**
     * 是否完成备货
     */
    @TableField("is_stock_up")
    private Boolean isStockUp;

    /**
     * 是否完成受理
     */
    @TableField("is_acceptance")
    private Boolean isAcceptance;

    /**
     * 是否紧急
     */
    @TableField("is_pressing")
    private Boolean isPressing;

    /**
     * 负责人
     */
    @TableField("owner")
    private Long owner;

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
