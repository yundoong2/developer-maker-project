package com.project.dmaker.dto;


import com.project.dmaker.code.StatusCode;
import com.project.dmaker.entity.Developer;
import com.project.dmaker.type.DeveloperLevel;
import com.project.dmaker.type.DeveloperSkillType;
import lombok.*;


/**
 * 개발자 상세 정보 DTO
 * @author cyh68
 * @since 2023-05-08
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeveloperDetailDto {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private Integer experienceYears;
    //재직 상태
    private StatusCode statusCode;
    private String memberId;
    private String name;
    private Integer age;

    /**
     * Developer -> DeveloperDetailDto 객체 변환 메소드
     * @param developer {@link Developer}
     * @return DeveloperDetailDto {@link DeveloperDetailDto}
     * @author cyh68
     * @since 2023-05-08
     **/
    public static DeveloperDetailDto fromEntity(Developer developer) {
        return DeveloperDetailDto.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .experienceYears(developer.getExperienceYears())
                .statusCode(developer.getStatusCode())
                .memberId(developer.getMemberId())
                .name(developer.getName())
                .age(developer.getAge())
                .build();
    }
}
