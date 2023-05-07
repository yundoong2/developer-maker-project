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

@Slf4j
@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        return CreateDeveloper.Response.fromEntity(
                developerRepository.save(createDeveloperFromRequest(request))
        );
    }

    @Transactional(readOnly = true)
    public List<DeveloperDto> getAllEmployedDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream()
                .map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeveloperDetailDto getDeveloperDetail(String memberId) {
        return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
    }

    @Transactional
    public DeveloperDetailDto updateDeveloper(String memberId, UpdateDeveloper.Request request) {

        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        return DeveloperDetailDto.fromEntity(
                setDeveloperFromRequest(request, getDeveloperByMemberId(memberId))
        );
    }

    private static Developer setDeveloperFromRequest(UpdateDeveloper.Request request, Developer developer) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return developer;
    }

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


    private void validateCreateDeveloperRequest(@NonNull CreateDeveloper.Request request) {
        //business validation
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent(developer -> {
                    throw new DMakerException(DMakerErrorCode.DUPLICATED_MEMBER_ID);
                });
    }

    private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {

        if (experienceYears < developerLevel.getMinExperienceYears() ||
                experienceYears > developerLevel.getMaxExperienceYears()) {
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    private static void validateDeveloperLevelV2(DeveloperLevelV2 developerLevel, Integer experienceYears) {

        developerLevel.validateExperienceYears(experienceYears);
    }

    private Developer getDeveloperByMemberId(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> {
                    throw new DMakerException(DMakerErrorCode.NO_DEVELOPER);
                });
    }

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
