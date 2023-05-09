package com.project.dmaker.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dmaker.dto.CreateDeveloper;
import com.project.dmaker.dto.UpdateDeveloper;
import com.project.dmaker.type.DeveloperLevel;
import com.project.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.project.dmaker.type.DeveloperLevel.SENIOR;
import static com.project.dmaker.type.DeveloperSkillType.BACK_END;
import static com.project.dmaker.type.DeveloperSkillType.FRONT_END;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller 단위 Test - Fail Case
 * @author cyh68
 * @since 2023-05-09
 **/
@WebMvcTest(DMakerController.class)
public class DMakerControllerFailTest extends DMakerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * CreateDeveloper.Request Fail 샘플 데이터
     * @return CreateDeveloper.Request {@link CreateDeveloper.Request}
     * @author cyh68
     * @since 2023-05-09
     **/
    private static CreateDeveloper.Request getDefaultFailRequest() {
        //developerLevel is missing
        return CreateDeveloper.Request.builder()
                .developerSkillType(FRONT_END)
                .experienceYears(5)
                .memberId("member")
                .name("steve")
                .age(24)
                .build();
    }

    /**
     * UpdateDeveloper.Request Fail 샘플 데이터
     * @return UpdateDeveloper.Request {@link CreateDeveloper.Request}
     * @author cyh68
     * @since 2023-05-09
     **/
    private static UpdateDeveloper.Request getDefaultUpdateFailRequest() {
        //experienceYears is exceeded the maximum value
        return UpdateDeveloper.Request.builder()
                .developerLevel(SENIOR)
                .developerSkillType(BACK_END)
                .experienceYears(21)
                .build();
    }

    /**
     * 개발자 생성 Fail Test - invalid request
     * @author cyh68
     * @since 2023-05-09
     **/
    @Test
    void createDeveloperTest_failed_with_invalid_request() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(post("/create-developer")
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(getDefaultFailRequest())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * 개발자 정보 변경 Fail Test - invalid request
     * @author cyh68
     * @since 2023-05-09
     **/
    @Test
    void updateDeveloperTest_failed_with_invalid_request() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(put("/developer/{memberId}", "testId")
                        .contentType(contentType)
                        .content(objectMapper.writeValueAsString(getDefaultUpdateFailRequest())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
