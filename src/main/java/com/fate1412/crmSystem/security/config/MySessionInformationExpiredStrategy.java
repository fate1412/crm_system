package com.fate1412.crmSystem.security.config;

import com.alibaba.fastjson.JSON;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.ResultCode;
import com.fate1412.crmSystem.utils.ResultTool;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MySessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    
    //会话信息过期策略
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        JsonResult<?> result = ResultTool.fail(ResultCode.USER_ACCOUNT_USE_BY_OTHERS);
        HttpServletResponse httpServletResponse = event.getResponse();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
