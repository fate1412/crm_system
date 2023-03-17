package com.fate1412.crmSystem.exception;

import com.fate1412.crmSystem.utils.ResultCode;
import lombok.Data;

@Data
public class DataCheckingException extends RuntimeException{
    ResultCode resultCode;

    public DataCheckingException(String msg) {
        super(msg);
    }
    
    public DataCheckingException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }
}
