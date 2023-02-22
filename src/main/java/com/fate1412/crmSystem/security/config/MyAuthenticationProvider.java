package com.fate1412.crmSystem.security.config;

import com.fate1412.crmSystem.security.service.impl.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class MyAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        // 获取用户的输入
//        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
//
//        // @TODO 验证密码,实际需要加密
//        String username = details.getUsername();
//        String dbPassword = myUserDetailService.loadUserByUsername(username).getPassword();
//        System.out.println("dbPassword = " + dbPassword);
//
//        String password = details.getPassword();
//        System.out.println("用户输入password = " + password);
//
//        if (!dbPassword.equals(password)) {
//            log.info("密码错误");
//            throw new BadCredentialsException("密码错误");
//        }
//
//        // @TODO 赋予权限,后期改成数据库权限
//        Collection<GrantedAuthority> auths =
//                (Collection<GrantedAuthority>) myUserDetailService.loadUserByUsername(username).getAuthorities();
//        return new UsernamePasswordAuthenticationToken(details.getUsername(), details.getPassword(), auths);
//    }
    
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
