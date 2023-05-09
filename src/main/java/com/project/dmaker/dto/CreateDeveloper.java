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
 * 개발자 생성 요청 및 응답 DTO
 * @author cyh68
 * @since 2023-05-08
 **/
public class CreateDeveloper {

    /**
     * 개발자 생성 요청 DTO
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
        //개발자 레벨
        @NotNull
        private DeveloperLevel developerLevel;
        //개발자 직무 타입
        @NotNull
        private DeveloperSkillType developerSkillType;
        //경력 연차
        @NotNull
        @Range(min = 0, max = 20)
        private Integer experienceYears;
        //고유 ID
        @NotNull
        @Size(min = 3, max = 50, message = "memberId size must be 3-50")
        private String memberId;
        //이름
        @NotNull
        @Size(min = 3, max = 20 , message = "name size must be 3-20")
        private String name;
        //나이
        @Min(18)
        private Integer age;
    }

    /**
     * 개발자 생성 응답 DTO
     * @author cyh68
     * @since 2023-05-08
     **/
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private DeveloperLevel developerLevel;
        private DeveloperSkillType developerSkillType;
        private Integer experienceYears;
        private String memberId;

        /**
         * Developer -> Response 객체 변환 메소드
         * @param developer {@link Developer}
         * @return Response {@link Response}
         * @author cyh68
         * @since 2023-05-08
         **/
        public static Response fromEntity(@NonNull Developer developer) {
            return Response.builder()
                    .developerLevel(developer.getDeveloperLevel())
                    .developerSkillType(developer.getDeveloperSkillType())
                    .experienceYears(developer.getExperienceYears())
                    .memberId(developer.getMemberId())
                    .build();
        }
    }
}
