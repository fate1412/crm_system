package com.fate1412.crmSystem.annotations;

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
        Input("Input",0),
        Select("Select",1),
        Date("Date",2),
        DateTime("DateTime",3);
        private String type;
        private Integer index;
    
        FormType(String type,Integer index) {
            this.type = type;
            this.index = index;
        }
    
        public String getType() {
            return type;
        }
    
        public void setType(String type) {
            this.type = type;
        }
    
        public Integer getIndex() {
            return index;
        }
    
        public void setIndex(Integer index) {
            this.index = index;
        }
    }
    
}
