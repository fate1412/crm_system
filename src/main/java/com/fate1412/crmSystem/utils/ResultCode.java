package com.fate1412.crmSystem.utils;

public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "成功"),
    
    /* 默认失败 */
    COMMON_FAIL(999, "失败"),
    
    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),
    PARAM_REPEAT(1005, "参数重复"),
    PARAM_MUST_ISNULL(3006,"未填写必备参数！"),
    
    /* 用户错误 */
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
    
    /* 业务错误 */
    NO_PERMISSION(3001, "没有权限"),
    UPDATE_ERROR(3002,"更新失败"),
    INSERT_ERROR(3003,"新增失败"),
    APPROVE(3004,"已审批"),
    INVOICE_ERROR1(3005,"已发货，不可修改！"),
    INVOICE_ERROR2(3006,"该订单无需再发货！"),
    STOCK_LIST_ERROR1(3007,"已备货，不可修改！"),
    STOCK_LIST_ERROR2(3008,"未完成受理，不可备货！"),
    DATA_NOT_FOUND(3009,"数据不存在！"),
    FLOW_NOT_FOUND(3010,"流程节点已被删除或改变，请联系管理员！");
    
    private Integer code;
    private String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * 根据code获取message
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }
}
