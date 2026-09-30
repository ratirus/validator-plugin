package com.validator.enums;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AttackTypeEnum {
    XSS("XS", "Cross Site Scripting"),
    SQLINJECTION("SI", "SQL Injection"),
    FILEUPLOAD("FU", "File Upload"),
    FILEDOWN("FD", "File Download"),
    URLCHANGE("UC", "권한없는 URL 접근"),
    CUSTOM("CU", "Custom Rule");
    // TIMEOUT("TO", "Timeout");

    private String code;
    private String description;


    public static List<AttackTypeEnum> getAllAttackTypes() {
        return Arrays.asList(AttackTypeEnum.values());
    }

    public static String getDescription(String code) {
        for (AttackTypeEnum type : AttackTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDescription();
            }
        }
        return "-";
    }

}
