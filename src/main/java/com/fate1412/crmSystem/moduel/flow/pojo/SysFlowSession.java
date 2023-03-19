package com.fate1412.crmSystem.moduel.flow.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class SysFlowSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

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
     * 表id
     */
    @TableField("table_name")
    private String tableName;
    
    
    /**
     * 数据id
     */
    @TableField("data_id")
    private Long dataId;

    /**
     * 状态
     */
    @TableField("pass")
    private Boolean pass;


}
