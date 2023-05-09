package com.project.dmaker.type;

import com.project.dmaker.exception.DMakerErrorCode;
import com.project.dmaker.exception.DMakerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.project.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.project.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;

/**
 * 개발자 레벨 Enum 클래스 V2 - 함수형 인터페이스 사용
 * @author cyh68
 * @since 2023-05-08
 **/
@AllArgsConstructor
@Getter
public enum DeveloperLevelV2 {
    NEW("신입 개발자", years -> years == 0),
    JUNIOR("주니어 개발자", years -> years <= MAX_JUNIOR_EXPERIENCE_YEARS),
    JUNGNIOR("중니어 개발자", years -> years > MAX_JUNIOR_EXPERIENCE_YEARS
            && years < MIN_SENIOR_EXPERIENCE_YEARS),
    SENIOR("시니어 개발자", years -> years >= MIN_SENIOR_EXPERIENCE_YEARS);

    //설명
    private final String description;

    //경력 연차 Validation 함수 정의
    private final Function<Integer, Boolean> validateFunction;

    /**
     * 함수형 인터페이스를 통한 경력 연차 Validation 메소드
     * @param years {@link Integer}
     * @author cyh68
     * @since 2023-05-08
     * @throws DMakerException
     **/ 
    public void validateExperienceYears(Integer years) {
        if (!validateFunction.apply(years)) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }
}
