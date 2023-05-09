package com.project.dmaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dmaker.dto.CreateDeveloper;
import com.project.dmaker.dto.DeveloperDetailDto;
import com.project.dmaker.dto.DeveloperDto;
import com.project.dmaker.dto.UpdateDeveloper;
import com.project.dmaker.entity.RetiredDeveloper;
import com.project.dmaker.service.DMakerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.project.dmaker.code.StatusCode.EMPLOYED;
import static com.project.dmaker.code.StatusCode.RETIRED;
import static com.project.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.project.dmaker.type.DeveloperLevel.JUNIOR;
import static com.project.dmaker.type.DeveloperLevel.SENIOR;
import static com.project.dmaker.type.DeveloperSkillType.BACK_END;
import static com.project.dmaker.type.DeveloperSkillType.FRONT_END;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller 단위 Test - Success Case
 *
 * @author cyh68
 * @since 2023-05-08
 **/
@WebMvcTest(DMakerController.class)
class DMakerControllerTest {

    //공통 ContentType
    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DMakerService dMakerService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * DeveloperDetailDto 샘플 데이터 (for Get, Put case)
     * @return DeveloperDetailDto {@link DeveloperDetailDto}
     * @author cyh68
     * @since 2023-05-09
     **/
    private static DeveloperDetailDto getDefaultDeveloperDetailDto() {
        return DeveloperDetailDto.builder()
                .developerLevel(SENIOR)
                .developerSkillType(FRONT_END)
                .experienceYears(MIN_SENIOR_EXPERIENCE_YEARS)
                .memberId("choi")
                .statusCode(EMPLOYED)
                .name("시니어")
                .age(30)
                .build();
    }

    /**
     * DeveloperDetailDto 샘플 데이터 (for Delete case)
     * @return DeveloperDetailDto {@link DeveloperDetailDto}
     * @author cyh68
     * @since 2023-05-09
     **/
    private static DeveloperDetailDto getDefaultDeletedDeveloperDetailDto() {
        return DeveloperDetailDto.builder()
                .developerLevel(SENIOR)
                .developerSkillType(FRONT_END)
                .experienceYears(MIN_SENIOR_EXPERIENCE_YEARS)
                .memberId("choi")
                .statusCode(RETIRED)
                .name("시니어")
                .age(30)
                .build();
    }

    /**
     * CreateDeveloper.Response 샘플 데이터
     * @return CreateDeveloper.Response {@link CreateDeveloper.Response}
     * @author cyh68
     * @since 2023-05-09
     **/
    private static CreateDeveloper.Response getDefaultResponse() {
        return CreateDeveloper.Response.builder()
                .developerLevel(JUNIOR)
                .developerSkillType(FRONT_END)
                .experienceYears(2)
                .memberId("kang2")
                .build();
    }

    /**
     * CreateDeveloper.Request 샘플 데이터
     * @return CreateDeveloper.Request {@link CreateDeveloper.Request}
     * @author cyh68
     * @since 2023-05-09
     **/
    private static CreateDeveloper.Request getDefaultRequest() {
        return CreateDeveloper.Request.builder()
                .developerLevel(SENIOR)
                .developerSkillType(BACK_END)
                .experienceYears(15)
                .memberId("kang")
                .name("yhc")
                .age(30)
                .build();
    }

    /**
     * UpdateDeveloper.Request 샘플 데이터
     * @return UpdateDeveloper.Request {@link UpdateDeveloper.Request}
     * @author cyh68
     * @since 2023-05-09
     **/
    private static UpdateDeveloper.Request getDefaultUpdateRequest() {
        return UpdateDeveloper.Request.builder()
                .developerLevel(SENIOR)
                .developerSkillType(FRONT_END)
                .experienceYears(10)
                .build();
    }

    /**
     * 개발자 리스트 조회 테스트
     *
     * @throws Exception
     * @author cyh68
     * @since 2023-05-08
     **/
    @Test
    void getAllDevelopersTest_success() throws Exception {
        //given
        DeveloperDto junior = DeveloperDto.builder()
                .developerLevel(JUNIOR)
                .developerSkillType(BACK_END)
                .memberId("memberId1")
                .build();

        DeveloperDto senior = DeveloperDto.builder()
                .developerLevel(SENIOR)
                .developerSkillType(BACK_END)
                .memberId("memberId2")
                .build();

        given(dMakerService.getAllEmployedDevelopers())
                .willReturn(Arrays.asList(junior, senior));

        //when
        //then
        mockMvc.perform(get("/developers").contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].developerSkillType", is(BACK_END.name())))
                .andExpect(jsonPath("$.[0].developerLevel", is(JUNIOR.name())))
                .andExpect(jsonPath("$.[1].developerSkillType", is(BACK_END.name())))
                .andExpect(jsonPath("$.[1].developerLevel", is(SENIOR.name()))
                );
    }

    /**
     * 개발자 상세 조회 테스트
     *
     * @author cyh68
     * @since 2023-05-08
     **/
    @Test
    void getDeveloperDetailTest_success() throws Exception {
        //given
        given(dMakerService.getDeveloperDetail(anyString()))
                .willReturn(getDefaultDeveloperDetailDto());

        //when
        //then
        mockMvc.perform(get("/developer/{memberId}", "choi").contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.developerLevel", is(SENIOR.name())))
                .andExpect(jsonPath("$.developerSkillType", is(FRONT_END.name())))
                .andExpect(jsonPath("$.experienceYears", is(MIN_SENIOR_EXPERIENCE_YEARS)))
                .andExpect(jsonPath("$.memberId", is("choi")))
                .andExpect(jsonPath("$.statusCode", is(EMPLOYED.name())));
    }

    /**
     * 개발자 생성 테스트
     *
     * @author cyh68
     * @since 2023-05-08
     **/
    @Test
    void createDeveloperTest_success() throws Exception {
        //given
        given(dMakerService.createDeveloper(any()))
                .willReturn(getDefaultResponse());

        //when
        //then
        mockMvc.perform(post("/create-developer")
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(getDefaultRequest())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.developerLevel", is(JUNIOR.name())))
                .andExpect(jsonPath("$.developerSkillType", is(FRONT_END.name())))
                .andExpect(jsonPath("$.experienceYears", is(2)))
                .andExpect(jsonPath("$.memberId", is("kang2")));
    }

    /**
     * 개발자 정보 변경 테스트
     *
     * @author cyh68
     * @since 2023-05-08
     **/
    @Test
    void updateDeveloperTest_success() throws Exception {
        //given
        given(dMakerService.updateDeveloper(anyString(), any()))
                .willReturn(getDefaultDeveloperDetailDto());
        //when
        //then
        mockMvc.perform(put("/developer/{memberId}", "choi")
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(getDefaultUpdateRequest())))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.developerLevel", is(SENIOR.name())))
                .andExpect(jsonPath("$.developerSkillType", is(FRONT_END.name())))
                .andExpect(jsonPath("$.experienceYears", is(MIN_SENIOR_EXPERIENCE_YEARS)))
                .andExpect(jsonPath("$.memberId", is("choi")))
                .andExpect(jsonPath("$.statusCode", is(EMPLOYED.name())));
    }

    /**
     * 개발자 삭제 테스트
     *
     * @author cyh68
     * @since 2023-05-08
     **/
    @Test
    void deleteDeveloperTest_success() throws Exception {
        //given
        given(dMakerService.deleteDeveloper(anyString()))
                .willReturn(getDefaultDeletedDeveloperDetailDto());
        //when
        //then
        mockMvc.perform(delete("/developer/{memberId}", "choi")
                        .contentType(contentType))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.statusCode", is(RETIRED.name())));
    }
}