package com.fate1412.crmSystem.moduel.flow.dto.select;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
@TableName("sys_flow_point")
public class SysFlowPointSelectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 流程id
     */
    @TableField("flow_id")
    private Long flowId;

    /**
     * 流程节点
     */
    @TableField("panel_point")
    private Integer panelPoint;

    /**
     * 审批人
     */
    @TableField("approver")
    private Long approver;
    
    private String approverR;

    /**
     * 下一个节点(-1表示没有下一个节点)
     */
    @TableField("next_point")
    private Long nextPoint;


}
