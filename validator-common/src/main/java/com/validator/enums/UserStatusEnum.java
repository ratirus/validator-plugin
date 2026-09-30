package com.validator.enums;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserStatusEnum {

    ACTIVE("A", "활성"),
    INACTIVE("I", "비활성"),
    PAUSE("P", "중지"),
    DELETE("D", "삭제");

    private String code;
    private String description;

    
    public static List<UserStatusEnum> getAllStatusTypes() {
        return Arrays.asList(UserStatusEnum.values());
    }

    public static String getDescription(String code) {
        for (UserStatusEnum type : UserStatusEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDescription();
            }
        }
        return "Unknown";
    }

}
