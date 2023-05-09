package com.project.dmaker.dto;

import com.project.dmaker.exception.DMakerErrorCode;
import lombok.*;

/**
 * 에러 응답 DTO
 * @author cyh68
 * @since 2023-05-08
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DMakerErrorResponse {
    private DMakerErrorCode errorCode;
    private String errorMessage;
}
