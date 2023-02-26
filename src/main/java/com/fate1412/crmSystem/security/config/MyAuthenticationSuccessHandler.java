package com.fate1412.crmSystem.security.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fate1412.crmSystem.security.mapper.SysUserMapper;
import com.fate1412.crmSystem.security.pojo.SysUser;
import com.fate1412.crmSystem.security.service.ISysUserService;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.JwtTokenUtils;
import com.fate1412.crmSystem.utils.ResultTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static ISysUserService userService;
    
    @Resource
    public void setUserService(ISysUserService userService) {
        MyAuthenticationSuccessHandler.userService = userService;
    }
    
    //登录成功处理逻辑
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //更新用户表上次登录时间、更新时间等字段
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = userService.getByUserName(userDetails.getUsername());
        sysUser.setLastLoginTime(LocalDateTime.now());
        sysUser.setUpdateTime(LocalDateTime.now());
        userService.updateById(sysUser);
    
        //此处还可以进行一些处理，比如登录成功之后可能需要返回给前台当前用户有哪些菜单权限，
        //进而前台动态的控制菜单的显示等，具体根据自己的业务需求进行扩展
        
        
    
        //返回json数据
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("token","admin");
        JsonResult<?> result = ResultTool.success(jsonObject);
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        response.getWriter().write(JSON.toJSONString(result));
    }
}
