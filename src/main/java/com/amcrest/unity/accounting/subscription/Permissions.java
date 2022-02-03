package com.amcrest.unity.accounting.subscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Permissions {
    CLOUD_STORAGE("cloud:storage"),
    ALERTS_SEND("alert:send");

    private String permission;
}
