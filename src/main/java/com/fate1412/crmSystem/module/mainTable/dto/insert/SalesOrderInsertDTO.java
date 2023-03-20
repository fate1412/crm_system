package com.fate1412.crmSystem.module.mainTable.dto.insert;

import com.fate1412.crmSystem.annotations.TableTitle;
import com.fate1412.crmSystem.annotations.TableTitle.FormType;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.mainTable.dto.child.SalesOrderChild;
import com.fate1412.crmSystem.utils.IdToName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 销售订单
 * </p>
 *
 * @author fate1412
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalesOrderInsertDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableTitle(value = "销售订单Id", fixed = true, disabled = true)
    private Long id;
    
    /**
     * 是否通过
     */
    @TableTitle("是否通过")
    private Boolean isPass;
    
    /**
     * 客户id
     */
    private IdToName customerIdR = new IdToName(TableNames.customer);
    
    @TableTitle(value = "客户id", link = true, disabled = true, formType = FormType.Select)
    private Long customerId;
    
    /**
     * 发货状态
     */
    @TableTitle("发货状态")
    private Integer invoiceStatus;

    
    private List<SalesOrderChild> childList;
    
    
}
