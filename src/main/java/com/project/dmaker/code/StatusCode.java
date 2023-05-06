package com.project.dmaker.code;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusCode {
    EMPLOYED("고용"),
    RETIRED("퇴직");

    private final String description;
}
