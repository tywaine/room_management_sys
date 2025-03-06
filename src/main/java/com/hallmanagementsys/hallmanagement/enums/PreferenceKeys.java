package com.hallmanagementsys.hallmanagement.enums;

import lombok.Getter;

@Getter
public enum PreferenceKeys {
    USER_USERNAME("HallManagementSystem_UserEmail"),
    USER_PASSWORD("HallManagementSystem_UserPassword");
    private final String key;

    PreferenceKeys(String key) {
        this.key = key;
    }

}
