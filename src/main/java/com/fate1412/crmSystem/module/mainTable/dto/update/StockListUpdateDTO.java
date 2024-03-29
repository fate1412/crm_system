package com.fate1412.crmSystem.module.mainTable.dto.update;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.module.mainTable.dto.child.StockListChild;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class StockListUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @TableTitle("备货单Id")
    private Long id;

    /**
     * 备货日期
     */
    @TableField("stock_up_date")
    @TableTitle(value = "备货日期",disabled = true,formType = TableTitle.FormType.Date)
    private Date stockUpDate;

    /**
     * 是否完成受理
     */
    @TableField("is_acceptance")
    @TableTitle("是否完成受理")
    private Boolean isAcceptance;

    /**
     * 负责人
     */
    @TableField("owner")
    private Long owner;
    
    @TableTitle(value = "负责人",link = true,disabled = true)
    private IdToName ownerR;
    
    private List<StockListChild> childList;

}
