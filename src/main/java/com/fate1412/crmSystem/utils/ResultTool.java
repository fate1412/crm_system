package com.fate1412.crmSystem.utils;

public class ResultTool {
    public static JsonResult<?> success() {
        return new JsonResult<>(true);
    }
    
    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(true, data);
    }
    
    public static JsonResult<Object> fail() {
        return new JsonResult<>(false);
    }
    
    public static JsonResult<Object> fail(ResultCode resultEnum) {
        return new JsonResult<>(false, resultEnum);
    }
    
    public static JsonResult<?> create(Boolean b) {
        if (b) {
            return success();
        }
        return fail();
    }
    
}
