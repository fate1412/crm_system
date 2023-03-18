package com.fate1412.crmSystem.moduel.flow.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("sys_flow_session")
public class SysFlowSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Long id;

    /**
     * 任务id
     */
    @TableField("task_id")
    private Integer taskId;

    /**
     * 流程id
     */
    @TableField("flow_id")
    private Long flowId;

    /**
     * 节点id
     */
    @TableField("point_id")
    private Long pointId;

    /**
     * 状态
     */
    @TableField("pass")
    private Boolean pass;


}
