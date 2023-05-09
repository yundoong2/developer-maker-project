package com.project.dmaker.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 개발자 직무 타입 Enum 클래스
 * @author cyh68
 * @since 2023-05-08
 **/
@AllArgsConstructor
@Getter
public enum DeveloperSkillType {
    BACK_END("백엔드 개발자"),
    FRONT_END("프론트엔드 개발자"),
    FULL_STACK("풀스택 개발자");

    private final String description;
}
