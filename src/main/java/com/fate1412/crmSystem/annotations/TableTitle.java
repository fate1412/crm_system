package com.fate1412.crmSystem.annotations;

import lombok.Data;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableTitle {
    String value();
    boolean fixed() default false;
    boolean link() default false;
    boolean disabled() default false;
    FormType formType() default FormType.Input;
    
    enum FormType {
        Input("input"),
        Select("select"),
        Date("date");
        private String type;
    
        FormType(String type) {
            this.type = type;
        }
    
        public String getType() {
            return type;
        }
    
        public void setType(String type) {
            this.type = type;
        }
    }
    
}
