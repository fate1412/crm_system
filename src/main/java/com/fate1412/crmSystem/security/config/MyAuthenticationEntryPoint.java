package com.fate1412.crmSystem.security.config;

import com.alibaba.fastjson.JSON;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.ResultCode;
import com.fate1412.crmSystem.utils.ResultTool;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    //匿名用户访问无权限资源时的异常
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        JsonResult<?> result = ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
    

}
