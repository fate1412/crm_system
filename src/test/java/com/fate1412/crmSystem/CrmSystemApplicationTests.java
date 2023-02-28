package com.fate1412.crmSystem;

import com.fate1412.crmSystem.mainTable.mapper.CustomerMapper;
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
    
    @Autowired
    private CustomerMapper customerMapper;
    
    @Test
    void contextLoads() {
    }
    
    @Test
    void test1() {

    }
    
}
