package com.validator.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachFile {

    private String fileName;
    private long fileSize;
    private String originFileName;
    private String extension;
    private String contentType;
    private String filePath;
}
