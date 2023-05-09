package com.project.dmaker.service;

import com.project.dmaker.code.StatusCode;
import com.project.dmaker.dto.CreateDeveloper;
import com.project.dmaker.dto.DeveloperDetailDto;
import com.project.dmaker.dto.DeveloperDto;
import com.project.dmaker.dto.UpdateDeveloper;
import com.project.dmaker.entity.Developer;
import com.project.dmaker.entity.RetiredDeveloper;
import com.project.dmaker.exception.DMakerErrorCode;
import com.project.dmaker.exception.DMakerException;
import com.project.dmaker.repository.DeveloperRepository;
import com.project.dmaker.repository.RetiredDeveloperRepository;
import com.project.dmaker.type.DeveloperLevel;
import com.project.dmaker.type.DeveloperLevelV2;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 각 요청에 대한 비지니스 로직 처리 Layer
 * @author cyh68
 * @since 2023-05-08
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    /**
     * 개발자 생성
     * @param request {@link CreateDeveloper.Request}
     * @return CreateDeveloper.Response {@link CreateDeveloper.Response}
     * @author cyh68
     * @since 2023-05-08
     * @throws DMakerException
     **/
    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        return CreateDeveloper.Response.fromEntity(
                developerRepository.save(createDeveloperFromRequest(request))
        );
    }

    /**
     * 개발자 리스트 조회
     * @return List(DeveloperDto) {@link List}
     * @author cyh68
     * @since 2023-05-08
     **/
    @Transactional(readOnly = true)
    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream()
                .map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 개발자 상세 조회
     * @param memberId {@link String}
     * @return DeveloperDetailDto {@link DeveloperDetailDto}
     * @author cyh68
     * @since 2023-05-08
     **/
    @Transactional(readOnly = true)
    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
    }

    /**
     * 개발자 정보 변경
     * @param memberId {@link String}
     * @param request {@link UpdateDeveloper.Request}
     * @return DeveloperDetailDto {@link DeveloperDetailDto}
     * @author cyh68
     * @since 2023-05-08
     * @throws
     **/
    @Transactional
    public DeveloperDetailDto updateDeveloper(String memberId, UpdateDeveloper.Request request) {

        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(
                setDeveloperFromRequest(request, getDeveloperByMemberId(memberId))
        );
    }

    /**
     * 개발자 삭제
     * <p>
     *     실제 삭제가 아닌, DEVELOPER 테이블에 StatusCode를 RETIRED로 변경, RETIRED_DEVELOPER 테이블에 INSERT
     * </p>
     * @param memberId {@link String}
     * @return DeveloperDetailDto {@link DeveloperDetailDto}
     * @author cyh68
     * @since 2023-05-08
     **/
    @Transactional
    public DeveloperDetailDto deleteDeveloper(String memberId) {
        // 1. EMPLOYER -> RETIRED
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(DMakerErrorCode.NO_DEVELOPER));

        developer.setStatusCode(StatusCode.RETIRED);

        // 2. save into RetiredDeveloper
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(memberId)
                .name(developer.getName())
                .build();

        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDto.fromEntity(developer);
    }

    /**
     * 개발자 정보 변경 메소드
     * @param request {@link UpdateDeveloper.Request}
     * @param developer {@link Developer}
     * @return Developer {@link Developer}
     * @author cyh68
     * @since 2023-05-08
     **/
    private static Developer setDeveloperFromRequest(UpdateDeveloper.Request request, Developer developer) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return developer;
    }

    /**
     * 개발자 생성 요청 정보에 대한 Validation
     * @param request {@link CreateDeveloper.Request}
     * @author cyh68
     * @since 2023-05-08
     * @throws DMakerException
     **/
    private void validateCreateDeveloperRequest(@NonNull CreateDeveloper.Request request) {
        //business validation
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent(developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);
                });
    }

    /**
     * 개발자 레벨 및 경력 Validation
     * @param developerLevel {@link DeveloperLevel}
     * @param experienceYears {@link Integer}
     * @author cyh68
     * @since 2023-05-08
     * @throws DMakerException
     **/
    private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {

        if (experienceYears < developerLevel.getMinExperienceYears() ||
                experienceYears > developerLevel.getMaxExperienceYears()) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    /**
     * 개발자 레벨 및 경력 Validation - 함수형 인터페이스 사용
     * @param developerLevel {@link DeveloperLevelV2}
     * @param experienceYears {@link Integer}
     * @author cyh68
     * @since 2023-05-08
     * @throws 
     **/
    private static void validateDeveloperLevelV2(DeveloperLevelV2 developerLevel, Integer experienceYears) {

        developerLevel.validateExperienceYears(experienceYears);
    }

    /**
     * memberId로 개발자 찾기
     * @param memberId {@link String}
     * @return Developer {@link Developer}
     * @author cyh68
     * @since 2023-05-08
     * @throws DMakerException
     **/
    private Developer getDeveloperByMemberId(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> {
                    throw new DMakerException(DMakerErrorCode.NO_DEVELOPER);
                });
    }

    /**
     * CreateDeveloper.Request -> Developer 객체 변환 메소드
     * @param request {@link CreateDeveloper.Request}
     * @return Developer {@link Developer}
     * @author cyh68
     * @since 2023-05-08
     **/
    private static Developer createDeveloperFromRequest(CreateDeveloper.Request request) {

        return Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .statusCode(StatusCode.EMPLOYED)
                .build();
    }
}
