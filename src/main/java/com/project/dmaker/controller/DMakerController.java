package com.project.dmaker.controller;

import com.project.dmaker.dto.CreateDeveloper;
import com.project.dmaker.dto.DeveloperDetailDto;
import com.project.dmaker.dto.DeveloperDto;
import com.project.dmaker.dto.UpdateDeveloper;
import com.project.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 각 HTTP 요청에 대한 처리 Controller
 * @author cyh68
 * @since 2023-05-08
 **/
@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {

    private final DMakerService dMakerService;

    /**
     * 모든 개발자 조회 요청 메소드
     * @return List(DeveloperDto) {@link List}
     * @author cyh68
     * @since 2023-05-08
     **/
    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllEmployedDevelopers();
    }

    /**
     * 특정 개발자 조회 요청 메소드
     * @param memberId {@link String}
     * @return DeveloperDetailDto {@link DeveloperDetailDto}
     * @author cyh68
     * @since 2023-05-08
     **/
    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDto getDeveloperDetail(@PathVariable("memberId") final String memberId) {
        log.info("GET /developers/{memberId} HTTP/1.1");

        return dMakerService.getDeveloperDetail(memberId);
    }

    /**
     * 개발자 생성 요청 메소드
     * @param request {@link CreateDeveloper.Request}
     * @return CreateDeveloper.Response {@link CreateDeveloper.Response}
     * @author cyh68
     * @since 2023-05-08
     **/
    @PostMapping("/create-developer")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateDeveloper.Response createDeveloper(@Validated @RequestBody final CreateDeveloper.Request request) {
        log.info("request = {}", request);

        return dMakerService.createDeveloper(request);
    }

    /**
     * 특정 개발자 정보 변경 요청 메소드
     * @param memberId {@link String}
     * @param request {@link UpdateDeveloper.Request}
     * @return DeveloperDetailDto {@link DeveloperDetailDto}
     * @author cyh68
     * @since 2023-05-08
     **/
    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDto updateDeveloper(@PathVariable("memberId") final String memberId,
                                              @Validated @RequestBody final UpdateDeveloper.Request request) {
        log.info("requestDto = {}", request);

        return dMakerService.updateDeveloper(memberId, request);
    }

    /**
     * 특정 개발자 삭제 요청 메소드
     * @param memberId {@link String}
     * @return DeveloperDetailDto {@link DeveloperDetailDto}
     * @author cyh68
     * @since 2023-05-08
     **/
    @DeleteMapping("/developer/{memberId}")
    public DeveloperDetailDto deleteDeveloper(@PathVariable("memberId") final String memberId) {

        return dMakerService.deleteDeveloper(memberId);
    }
}
