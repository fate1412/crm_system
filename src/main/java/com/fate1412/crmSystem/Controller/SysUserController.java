package com.fate1412.crmSystem.Controller;

import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class SysUserController {
    @Autowired
    private ISysUserService sysUserService;
    
    @PostMapping("/login")
    @PreAuthorize("hasAnyAuthority(\"TEST_R1\")")
    public String login(@RequestBody SysUser sysUser) {
        SysUser djj = sysUserService.getByUserName("djj");
        log.info(djj.toString());
        return "djj";
    }
}
