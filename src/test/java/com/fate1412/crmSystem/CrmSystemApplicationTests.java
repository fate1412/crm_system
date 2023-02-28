package com.fate1412.crmSystem;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.mapper.CustomerMapper;
import com.fate1412.crmSystem.pojo.Customer;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.MyCollections;
import com.fate1412.crmSystem.utils.SQLFactor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

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
//        Customer customer = customerMapper.selectById(1);
        List<SQLFactor<?>> list = new ArrayList<>();
        list.add(new SQLFactor<>("id","EQ",1L));
        list.add(new SQLFactor<>("OR"));
        list.add(new SQLFactor<>("name","EQ","djj"));
        List<JSONObject> customer = customerMapper.selectAll("customer",list);
        JSONArray jsonArray = new JSONArray();
        log.info(customer.toString());
    }
    
}
