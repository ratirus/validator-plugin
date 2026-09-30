package com.validator.enums;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RuleTypeEnum {
    DEFAULT("D", "Default Rule"),
    GET("G", "GET Rule"),
    POST("P", "POST Rule"),
    CUSTOM("C", "Custom Rule"),
    URL("U", "Auth URL Rule");

    private String code;
    private String description;

    public static List<RuleTypeEnum> getAllRuleTypes() {
        return Arrays.asList(RuleTypeEnum.values());
    }

    public static String getDescription(String code) {
        for (RuleTypeEnum type : RuleTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDescription();
            }
        }
        return "-";
    }
}
