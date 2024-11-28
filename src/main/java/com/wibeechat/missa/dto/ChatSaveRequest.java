package com.wibeechat.missa.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 메시지 저장 요청")
public class ChatSaveRequest implements Serializable {

    @Schema(
            description = "메시지 내용",
            example = "메시지 내용",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String content;

    @Schema(
            description = "발신자 유형",
            example = "USER",
            allowableValues = {"USER", "AI"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String sender;

    @Schema(
            description = "세션 ID",
            example = "abc123"
    )
    private String sessionId;  // 세션 ID 필드 추가
}