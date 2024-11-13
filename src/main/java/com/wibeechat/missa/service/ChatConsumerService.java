package com.wibeechat.missa.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibeechat.missa.dto.ChatSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class ChatConsumerService {
    private final KafkaConsumerService chatService;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper 주입
    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void consumeMessage(String message) {
        try {
            log.info("Received raw message: {}", message);

            // JSON 문자열을 Map으로 변환
            Map<String, String> messageMap = objectMapper.readValue(message, Map.class);

            // Python에서 보낸 메시지 형식을 ChatSaveRequest로 변환
            ChatSaveRequest request = ChatSaveRequest.builder()
                    .content(messageMap.get("content"))
                    .sender(messageMap.get("sender"))
                    .build();

            // 시스템 기본값 사용
            String userNo = "system";

            log.info("Processing message - content: {}, sender: {}",
                    request.getContent(), request.getSender());

            chatService.saveMessage(userNo, request);
            log.info("Message processed successfully");

        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}

