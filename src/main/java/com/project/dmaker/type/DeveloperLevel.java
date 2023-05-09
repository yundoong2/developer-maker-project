package com.project.dmaker.type;

import com.project.dmaker.dto.DMakerErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.project.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.project.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.project.dmaker.exception.DMakerErrorCode.NULL_VALUE;

/**
 * 개발자 레벨 Enum 클래스
 * @author cyh68
 * @since 2023-05-08
 **/
@AllArgsConstructor
@Getter
@Slf4j
public enum DeveloperLevel {
    NEW("신입 개발자", 0, 0),
    JUNIOR("주니어 개발자", 1, MAX_JUNIOR_EXPERIENCE_YEARS),
    JUNGNIOR("중니어 개발자", MAX_JUNIOR_EXPERIENCE_YEARS + 1, MIN_SENIOR_EXPERIENCE_YEARS - 1),
    SENIOR("시니어 개발자", MIN_SENIOR_EXPERIENCE_YEARS,  50);

    //설명
    private final String description;
    //최소 경력연차
    private final Integer minExperienceYears;
    //최대 경력연차
    private final Integer maxExperienceYears;
}
