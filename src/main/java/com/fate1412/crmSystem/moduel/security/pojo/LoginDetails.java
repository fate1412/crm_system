package com.fate1412.crmSystem.moduel.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDetails {
    private String username;
    private String password;
}
