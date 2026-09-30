package com.validator.enums;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogTypeEnum {
    USER("U", "사용자 공격"),
    // BOT("b", "자동화 공격 로그"),
    DIRECT("D", "Bot/자동화 공격(인증키 미존재)"),

    PARAMS("P", "Parameter 변조(인증키 불일치)"),

    MOVE("M", "관리자 선정"),

    TIMEOUT("T", "Request Timeout");

    private String code;
    private String description;

    public static List<LogTypeEnum> getAllLogTypes() {
        return Arrays.asList(LogTypeEnum.values());
    }

    public static String getDescription(String code) {
        for (LogTypeEnum type : LogTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDescription();
            }
        }
        return "Unknown";
    }


}
