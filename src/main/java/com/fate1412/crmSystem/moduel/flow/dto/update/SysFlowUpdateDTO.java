package com.fate1412.crmSystem.moduel.flow.dto.update;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class SysFlowUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 流程名称
     */
    @TableField("name")
    private String name;

    /**
     * 关联表
     */
    @TableField("relevance_table")
    private Integer relevanceTable;
    
    private IdToName relevanceTableR;

    /**
     * 触发动作
     */
    @TableField("trigger_action")
    private Integer triggerAction;

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
    
    private IdToName createrR;

    /**
     * 修改人
     */
    @TableField("updater")
    private Long updater;
    
    private IdToName updaterR;


}
