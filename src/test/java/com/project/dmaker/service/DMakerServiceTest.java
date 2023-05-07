package com.project.dmaker.service;

import com.project.dmaker.code.StatusCode;
import com.project.dmaker.dto.CreateDeveloper;
import com.project.dmaker.dto.DeveloperDetailDto;
import com.project.dmaker.entity.Developer;
import com.project.dmaker.exception.DMakerErrorCode;
import com.project.dmaker.exception.DMakerException;
import com.project.dmaker.repository.DeveloperRepository;
import com.project.dmaker.repository.RetiredDeveloperRepository;
import com.project.dmaker.type.DeveloperLevel;
import com.project.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.project.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.project.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.project.dmaker.type.DeveloperLevel.*;
import static com.project.dmaker.type.DeveloperSkillType.FRONT_END;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;
    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private static Developer getDeveloper() {
        return Developer.builder()
                .developerLevel(SENIOR)
                .developerSkillType(FRONT_END)
                .experienceYears(12)
                .memberId("memberId")
                .statusCode(StatusCode.EMPLOYED)
                .name("name")
                .age(12)
                .build();
    }

    private static CreateDeveloper.Request getDefaultCreateRequest(
            DeveloperLevel developerLevel,
            DeveloperSkillType developerSkillType,
            Integer experienceYears
    ) {
        CreateDeveloper.Request request = CreateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .experienceYears(experienceYears)
                .memberId("memberId")
                .name("name")
                .age(35)
                .build();
        return request;
    }

    @Test
    public void getDeveloperDetail() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(getDeveloper()));

        //when
        DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

        //then
        assertEquals(SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(FRONT_END, developerDetail.getDeveloperSkillType());
        assertEquals(12, developerDetail.getExperienceYears());
    }

    @Test
    void createDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        given(developerRepository.save(any()))
                .willReturn(getDeveloper());

        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);
        //when
        CreateDeveloper.Response developer = dMakerService.createDeveloper(
                getDefaultCreateRequest(SENIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS)
        );

        //then
        verify(developerRepository, times(1))
                .save(captor.capture());
        Developer savedDeveloper = captor.getValue();

        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());
    }

    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(getDeveloper()));

        //when
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getDefaultCreateRequest(SENIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS))
        );

        //then
        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }

    @Test
    void createDeveloperTest_failed_with_unmatched_level() {
        //given
        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getDefaultCreateRequest(JUNIOR, FRONT_END, MAX_JUNIOR_EXPERIENCE_YEARS + 1))
        );

        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,
                dMakerException.getDMakerErrorCode());


        dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getDefaultCreateRequest(JUNGNIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS + 1))
        );

        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,
                dMakerException.getDMakerErrorCode());

        dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getDefaultCreateRequest(SENIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS - 1))
        );

        assertEquals(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,
                dMakerException.getDMakerErrorCode());
    }
}