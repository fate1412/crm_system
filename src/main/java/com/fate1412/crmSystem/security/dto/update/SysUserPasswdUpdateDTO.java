package com.fate1412.crmSystem.security.dto.update;

import lombok.Data;

@Data
public class SysUserPasswdUpdateDTO {
    
    /**
     * 主键ID
     */
    private Long userId;
    
    private String password;
}
