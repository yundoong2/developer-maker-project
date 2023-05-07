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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(12)
                .memberId("memberId")
                .statusCode(StatusCode.EMPLOYED)
                .name("name")
                .age(12)
                .build();
    }

    private static CreateDeveloper.Request getDefaultCreateRequest() {
        CreateDeveloper.Request request = CreateDeveloper.Request.builder()
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(12)
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
        assertEquals(DeveloperLevel.SENIOR, developerDetail.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, developerDetail.getDeveloperSkillType());
        assertEquals(12, developerDetail.getExperienceYears());
    }

    @Test
    void createDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());

        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);
        //when
        CreateDeveloper.Response developer = dMakerService.createDeveloper(getDefaultCreateRequest());

        //then
        verify(developerRepository, times(1))
                .save(captor.capture());
        Developer savedDeveloper = captor.getValue();

        assertEquals(DeveloperLevel.SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(12, savedDeveloper.getExperienceYears());
    }



    @Test
    void createDeveloperTest_failed_with_duplicated() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(getDeveloper()));

        //when
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getDefaultCreateRequest())
        );

        //then
        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }
}