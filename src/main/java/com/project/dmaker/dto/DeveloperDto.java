package com.project.dmaker.dto;

import com.project.dmaker.entity.Developer;
import com.project.dmaker.type.DeveloperLevel;
import com.project.dmaker.type.DeveloperSkillType;
import lombok.*;

/**
 * 개발자 정보 DTO
 * @author cyh68
 * @since 2023-05-08
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DeveloperDto {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private String memberId;

    /**
     * Developer -> DeveloperDto 객체 변환 메소드
     * @param developer {@link Developer}
     * @return DeveloperDto {@link DeveloperDto}
     * @author cyh68
     * @since 2023-05-08
     **/
    public static DeveloperDto fromEntity(Developer developer) {
        return DeveloperDto.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .memberId(developer.getMemberId())
                .build();
    }
}
