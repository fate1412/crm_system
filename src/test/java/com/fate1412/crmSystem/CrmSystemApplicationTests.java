package com.fate1412.crmSystem;

import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class CrmSystemApplicationTests {
    @Autowired
    private ISysUserService sysUserService;
    
    @Test
    void contextLoads() {
    }
    
    @Test
    void test1() {
        SysUser sysUser = sysUserService.getById(1);
        log.info("" + sysUser);
    }
    
}
