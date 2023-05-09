package com.project.dmaker.entity;

import com.project.dmaker.code.StatusCode;
import com.project.dmaker.type.DeveloperLevel;
import com.project.dmaker.type.DeveloperSkillType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 개발자 Entity
 * @author cyh68
 * @since 2023-05-08
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "DEVELOPER")
public class Developer {

    //고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    //개발자 레벨
    @Enumerated(EnumType.STRING)
    private DeveloperLevel developerLevel;

    //개발자 직무 타입
    @Enumerated(EnumType.STRING)
    private DeveloperSkillType developerSkillType;

    //경력 연차
    private Integer experienceYears;

    //고유 멤버 ID
    private String memberId;
    
    //이름
    private String name;
    
    //나이
    private Integer age;

    //직무 상태
    @Enumerated(EnumType.STRING)
    private StatusCode statusCode;

    //생성 날짜
    @CreatedDate
    private LocalDateTime createAt;
    
    //마지막 변경 날짜
    @LastModifiedDate
    private LocalDateTime updateAt;
}
