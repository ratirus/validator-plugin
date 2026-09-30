package com.validator.web.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageVo {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer navigatePages = 10;
    
}
