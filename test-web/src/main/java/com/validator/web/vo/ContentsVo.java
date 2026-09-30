package com.validator.web.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentsVo {
   
   private String title;
   private String contents;
   private String filename;
   private String filepath;
   private String type;
   private int idx; 
}
