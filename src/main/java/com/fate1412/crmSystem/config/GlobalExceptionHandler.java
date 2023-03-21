package com.fate1412.crmSystem.config;

import com.fate1412.crmSystem.exception.DataCheckingException;
import com.fate1412.crmSystem.utils.JsonResult;
import com.fate1412.crmSystem.utils.ResultCode;
import com.fate1412.crmSystem.utils.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    
    /**
     * 数据校验异常
     */
    @ExceptionHandler(value = DataCheckingException.class)
    public JsonResult<?> DataCheckingExceptionHandler(DataCheckingException e) {
//        log.error("数据校验异常！ msg: -> ", e);
        return ResultTool.fail(e.getResultCode());
    }
    
    
    /**
     * 空指针异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    public JsonResult<?> NullPointerExceptionHandler(NullPointerException e) {
        log.error("数据校验异常！ msg: -> ", e);
        return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
    }
}
