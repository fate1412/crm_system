package com.fate1412.crmSystem.module.flow.dto.select;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@TableName("sys_flow_session")
public class SysFlowSessionSelectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @TableTitle("任务id")
    private Long id;

    /**
     * 流程id
     */
    @TableField("flow_id")
    @TableTitle("流程id")
    private Long flowId;

    /**
     * 节点id
     */
    @TableField("point_id")
    private Long pointId;
    
    
    /**
     * 表
     */
    @TableField("table_name")
    @TableTitle("所属业务")
    private String tableName;
    
    private String tableNameR;
    
    
    /**
     * 数据id
     */
    @TableField("data_id")
    @TableTitle("流程数据")
    private Long dataId;
    
    /**
     * 状态
     */
    @TableField("pass")
    private Integer pass;
    
    @TableTitle("审批状态")
    private String passR;
    
    @TableTitle("提交人")
    private IdToName createrR;
    private Long creater;
    
    @TableTitle("下一审批人")
    private IdToName nextApproverR;
    private Long nextApprover;
    
    
    public String getPassString() {
        switch (pass) {
            case 0: return "未审批";
            case 1: return "已同意";
            default: return "已拒绝";
        }
    }

}
