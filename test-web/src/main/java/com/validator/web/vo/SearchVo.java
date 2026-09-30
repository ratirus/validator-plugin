package com.validator.web.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchVo extends PageVo{
    private String searchKeyword;
    private String type;
    
    public String getType(){
        if(type == null || type.equals("")){
            this.type = "1";
        }
        return type;
    }
}
