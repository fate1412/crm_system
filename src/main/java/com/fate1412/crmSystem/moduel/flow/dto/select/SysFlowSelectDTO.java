package com.fate1412.crmSystem.moduel.flow.dto.select;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.moduel.mainTable.constant.TableNames;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author fate1412
 * @since 2023-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysFlowSelectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @TableTitle(value = "流程Id",fixed = true,disabled = true)
    private Long id;

    /**
     * 流程名称
     */
    @TableField("name")
    @TableTitle(value = "流程名称",inserted = true)
    private String name;

    /**
     * 关联表
     */
    @TableField("relevance_table")
    @TableTitle(value = "关联表",inserted = true,disabled = true)
    private Integer relevanceTable;
    
    private IdToName relevanceTableR = new IdToName(TableNames.tableDict);

    /**
     * 触发动作
     */
    @TableField("trigger_action")
    @TableTitle(value = "触发动作",inserted = true,disabled = true)
    private Integer triggerAction;

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
    @TableTitle(value = "创建人",disabled = true,link = true)
    private Long creater;
    
    private IdToName createrR = new IdToName(TableNames.sysUser);

    /**
     * 修改人
     */
    @TableField("updater")
    @TableTitle(value = "修改人",disabled = true,link = true)
    private Long updater;
    
    private IdToName updaterR = new IdToName(TableNames.sysUser);


}
