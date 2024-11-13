package com.wibeechat.missa.dto;

// DTO 클래스 생성

import lombok.Data;

@Data
public class MessageDto {
    private String timestamp;
    private String userMessage;
    private String aiResponse;
    private String messageType;
}