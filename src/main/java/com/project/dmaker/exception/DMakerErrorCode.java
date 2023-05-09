package com.project.dmaker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorCode 정의 클래스
 * @author cyh68
 * @since 2023-05-08
 **/
@Getter
@AllArgsConstructor
public enum DMakerErrorCode {
    NO_DEVELOPER("해당되는 개발자가 없습니다."),
    DUPLICATED_MEMBER_ID("MemberID가 중복되는 개발자가 있습니다."),
    LEVEL_EXPERIENCE_YEARS_NOT_MATCHED("개발자 레발과 연차가 맞지 않습니다."),
    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다"),
    INVALID_REQUEST("잘못된 요청입니다."),
    NULL_VALUE("Null 값이 존재합니다.")
    ;

    private final String description;
}
