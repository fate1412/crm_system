package com.fate1412.crmSystem.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fate1412.crmSystem.security.pojo.LoginDetails;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MyUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/user/login",
            "POST");
    
    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    
    private boolean postOnly = true;
    
    public MyUsernamePasswordAuthenticationFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }
    
    public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
    }
    
    
    @Nullable
    protected LoginDetails obtainUsernameAndPassword(HttpServletRequest request) {
        // 获取请求内容
        Map<String, String> loginData = new HashMap<>(2);
        try {
            loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String username = loginData.get(getUsernameParameter());
        String password = loginData.get(getPasswordParameter());
        
        username = (username != null) ? username.trim() : "";
        password = (password != null) ? password : "";
        
        return new LoginDetails(username, password);
    }
    
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
    
    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }
    
    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }
    
    public final String getUsernameParameter() {
        return this.usernameParameter;
    }
    
    public final String getPasswordParameter() {
        return this.passwordParameter;
    }
    
    
    public boolean isPostOnly() {
        return postOnly;
    }
    
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }
    
    //自定义身份验证过滤器(json)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("不支持认证方式: " + request.getMethod());
        }
        
        // 获取请求内容
        LoginDetails user = obtainUsernameAndPassword(request);
        assert user != null;
        // 创建 Authentication
        
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(), user.getPassword());
        setDetails(request, authRequest);
        
        // 执行身份验证
        return getAuthenticationManager().authenticate(authRequest);
        
    }
    
    
}
