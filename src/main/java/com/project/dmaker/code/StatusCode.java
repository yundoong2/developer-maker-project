package com.project.dmaker.code;

import lombok.AllArgsConstructor;

/**
 * 개발자 재직 상태 Enum 클래스
 * @author cyh68
 * @since 2023-05-08
 **/
@AllArgsConstructor
public enum StatusCode {
    EMPLOYED("고용"),
    RETIRED("퇴직");

    private final String description;
}
