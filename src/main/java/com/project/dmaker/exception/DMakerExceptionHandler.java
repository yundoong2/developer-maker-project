package com.project.dmaker.exception;

import com.project.dmaker.dto.DMakerErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.project.dmaker.exception.DMakerErrorCode.*;

/**
 * Exception Handler 클래스
 *
 * @author cyh68
 * @since 2023-05-08
 **/
@Slf4j
@RestControllerAdvice
public class DMakerExceptionHandler {

    /**
     * DMakerException 예외 처리
     *
     * @param e       {@link DMakerException}
     * @param request {@link HttpServletRequest}
     * @return DMakerErrorResponse {@link DMakerErrorResponse}
     * @author cyh68
     * @since 2023-05-08
     **/
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DMakerException.class)
    public DMakerErrorResponse handleException(DMakerException e, HttpServletRequest request) {
        log.error("errorCode: {}, url: {}, message: {}",
                e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage());

        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    /**
     * HttpRequestMethodNotSupportedException, MethodArgumentNotValidException 예외 처리
     *
     * @param e       {@link Exception}
     * @param request {@link HttpServletRequest}
     * @return DMakerErrorResponse {@link DMakerErrorResponse}
     * @author cyh68
     * @since 2023-05-08
     **/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class
            , MethodArgumentNotValidException.class})
    public DMakerErrorResponse handleBadRequest(Exception e, HttpServletRequest request) {

        log.error("url : {}, message : {}", request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getDescription())
                .build();
    }

    /**
     * Exception 예외 처리
     *
     * @param e       {@link Exception}
     * @param request {@link HttpServletRequest}
     * @return DMakerErrorResponse {@link DMakerErrorResponse}
     * @author cyh68
     * @since 2023-05-08
     **/
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(Exception e, HttpServletRequest request) {

        log.error("url : {}, message : {}", request.getRequestURI(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getDescription())
                .build();
    }


}
