package com.fate1412.crmSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableWebSecurity()
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class CrmSystemApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CrmSystemApplication.class, args);
    }
    
}
