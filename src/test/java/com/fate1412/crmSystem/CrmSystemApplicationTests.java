package com.fate1412.crmSystem;

import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.module.customTable.mapper.TableDictMapper;
import com.fate1412.crmSystem.module.mainTable.constant.TableNames;
import com.fate1412.crmSystem.module.mainTable.mapper.CustomerMapper;
import com.fate1412.crmSystem.module.security.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class CrmSystemApplicationTests {
    @Autowired
    private ISysUserService sysUserService;
    
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private TableDictMapper tableDictMapper;
    
    @Test
    void contextLoads() {
    }
    
    @Test
    void test1() {
        List<JSONObject> select = tableDictMapper.select(TableNames.customer, null);
        log.info("select{}",select);
    }
    
}
