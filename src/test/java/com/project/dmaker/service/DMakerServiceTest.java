package com.project.dmaker.service;

import com.project.dmaker.code.StatusCode;
import com.project.dmaker.dto.CreateDeveloper;
import com.project.dmaker.dto.DeveloperDetailDto;
import com.project.dmaker.dto.UpdateDeveloper;
import com.project.dmaker.entity.Developer;
import com.project.dmaker.entity.RetiredDeveloper;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.project.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.project.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.project.dmaker.exception.DMakerErrorCode.*;
import static com.project.dmaker.type.DeveloperLevel.*;
import static com.project.dmaker.type.DeveloperSkillType.BACK_END;
import static com.project.dmaker.type.DeveloperSkillType.FRONT_END;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Service Layer 단위 테스트
 * @author cyh68
 * @since 2023-05-09
 **/
@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;
    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks
    private DMakerService dMakerService;

    /**
     * Developer 샘플 데이터
     * @return Developer {@link Developer}
     * @author cyh68
     * @since 2023-05-09
     **/
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

    /**
     * CreateDeveloper.Request 샘플 데이터
     * @return CreateDeveloper.Request {@link CreateDeveloper.Request}
     * @author cyh68
     * @since 2023-05-09
     **/
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

    private static UpdateDeveloper.Request getDefaultUpdateRequest(
            DeveloperLevel developerLevel, DeveloperSkillType developerSkillType, Integer experienceYears) {
        return UpdateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .experienceYears(experienceYears)
                .build();
    }

    /**
     * 개발자 조회 성공 테스트
     * @author cyh68
     * @since 2023-05-09
     **/
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

    /**
     * 개발자 생성 성공 테스트
     * @author cyh68
     * @since 2023-05-09
     **/
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
        assertEquals(MIN_SENIOR_EXPERIENCE_YEARS, savedDeveloper.getExperienceYears());
    }

    /**
     * 개발자 생성 실패 테스트 - 'memberId' duplicated
     * @author cyh68
     * @since 2023-05-09
     **/
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
        assertEquals(DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }

    /**
     * 개발자 생성 실패 테스트 - unmatched 'developerLevel' with 'experienceYears'
     * @author cyh68
     * @since 2023-05-09
     **/
    @Test
    void createDeveloperTest_failed_with_unmatched_level() {
        //given
        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getDefaultCreateRequest(JUNIOR, FRONT_END, MAX_JUNIOR_EXPERIENCE_YEARS + 1))
        );

        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,
                dMakerException.getDMakerErrorCode());


        dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getDefaultCreateRequest(JUNGNIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS + 1))
        );

        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,
                dMakerException.getDMakerErrorCode());

        dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.createDeveloper(getDefaultCreateRequest(SENIOR, FRONT_END, MIN_SENIOR_EXPERIENCE_YEARS - 1))
        );

        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,
                dMakerException.getDMakerErrorCode());
    }

    /**
     * 개발자 정보 변경 성공 테스트
     * @author cyh68
     * @since 2023-05-09
     **/
    @Test
    void updateDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(getDeveloper()));
        //when
        DeveloperDetailDto result =
                dMakerService.updateDeveloper("memberId", getDefaultUpdateRequest(SENIOR, BACK_END, 18));

        //then
        assertEquals(SENIOR, result.getDeveloperLevel());
        assertEquals(BACK_END, result.getDeveloperSkillType());
        assertEquals(18, result.getExperienceYears());
    }

    /**
     * 개발자 정보 변경 실패 케이스 - 기존 memberId 존재 X
     * @author cyh68
     * @since 2023-05-09
     **/
    @Test
    void updateDeveloperTest_failed_with_not_exist_memberId() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());

        //when
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.updateDeveloper("memberId", getDefaultUpdateRequest(SENIOR, BACK_END, 18))
        );

        assertEquals(NO_DEVELOPER, dMakerException.getDMakerErrorCode());
    }

    /**
     * 개발자 정보 변경 실패 케이스 - unmatched 'developerLevel' with 'experienceYears'
     * @author cyh68
     * @since 2023-05-09
     **/
    @Test
    void updateDeveloperTest_failed_with_unmatched_level() {
        //given
        //when
        DMakerException dMakerException = assertThrows(DMakerException.class,
                () -> dMakerService.updateDeveloper("memberId", getDefaultUpdateRequest(JUNIOR, BACK_END, 18))
        );

        //then
        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());
    }

    /**
     * 개발자 삭제 성공 테스트
     * @author cyh68
     * @since 2023-05-09
     **/
    @Test
    void deleteDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(getDeveloper()));
        //when
        DeveloperDetailDto deleteResult = dMakerService.deleteDeveloper("memberId");

        ArgumentCaptor<RetiredDeveloper> captor = ArgumentCaptor.forClass(RetiredDeveloper.class);

        verify(retiredDeveloperRepository, times(1))
                .save(captor.capture());
        RetiredDeveloper savedDeveloper = captor.getValue();

        //then
        assertEquals(StatusCode.RETIRED, deleteResult.getStatusCode());
        assertEquals("memberId", savedDeveloper.getMemberId());
        assertEquals("name", savedDeveloper.getName());
    }

    /**
     * 개발자 삭제 실패 테스트 - 기존 memberId 존재 X
     * @author cyh68
     * @since 2023-05-09
     **/
    @Test
    void deleteDeveloperTest_fail_with_not_exist_memberId() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        //when
        DMakerException dMakerException = assertThrows(DMakerException.class, () -> {
            dMakerService.deleteDeveloper("memberId");
        });

        //then
        assertEquals(NO_DEVELOPER, dMakerException.getDMakerErrorCode());
    }
}