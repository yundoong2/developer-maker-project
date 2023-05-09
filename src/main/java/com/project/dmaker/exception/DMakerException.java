package com.project.dmaker.exception;

import lombok.Getter;

/**
 * 커스텀 Exception 정의 클래스
 * @author cyh68
 * @since 2023-05-08
 **/
@Getter
public class DMakerException extends RuntimeException{
    //에러 코드
    private DMakerErrorCode dMakerErrorCode;
    //에러 상세 메시지
    private String detailMessage;

    public DMakerException(DMakerErrorCode errorCode) {
        super(errorCode.getDescription());
        this.dMakerErrorCode = errorCode;
        this.detailMessage = errorCode.getDescription();
    }

    public DMakerException(DMakerErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.dMakerErrorCode = errorCode;
        this.detailMessage = detailMessage;
    }
}
