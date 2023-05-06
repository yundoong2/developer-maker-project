package com.project.dmaker.controller;

import com.project.dmaker.dto.CreateDeveloper;
import com.project.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {

    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<String> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return Arrays.asList("steve", "chris", "john");
    }

    @PostMapping("/create-developer")
    public List<String> createDeveloper(@Validated @RequestBody CreateDeveloper.Request request ) {
        log.info("request = {}", request);

        dMakerService.createDeveloper(request);

        return Collections.singletonList("steve");
    }
}
