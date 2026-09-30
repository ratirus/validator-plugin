package com.validator.web.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileContentsVo {
   
   private String title;
   private String contents;
   private String fileName;
   private String filePath;
   private long fileSize;
   private String fileExt;
   private String type;

   private int idx; 


   

}
