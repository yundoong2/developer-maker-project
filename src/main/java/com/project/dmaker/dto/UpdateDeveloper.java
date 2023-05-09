package com.project.dmaker.dto;

import com.project.dmaker.entity.Developer;
import com.project.dmaker.type.DeveloperLevel;
import com.project.dmaker.type.DeveloperSkillType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

/**
 * 개발자 정보 변경 DTO
 * @author cyh68
 * @since 2023-05-08
 **/
public class UpdateDeveloper {

    /**
     * 개발자 정보 변경 요청 DTO
     * @author cyh68
     * @since 2023-05-08
     **/
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request {
        @NotNull
        private DeveloperLevel developerLevel;
        @NotNull
        private DeveloperSkillType developerSkillType;
        @NotNull
        @Range(min = 0, max = 20)
        private Integer experienceYears;
    }
}
