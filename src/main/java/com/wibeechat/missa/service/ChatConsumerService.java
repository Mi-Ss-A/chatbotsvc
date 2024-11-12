package com.wibeechat.missa.service;


import com.wibeechat.missa.dto.ChatSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

// 카프카 컨슈머 서비스
@Service
@Slf4j
@RequiredArgsConstructor
public class ChatConsumerService {
    private final KafkaConsumerService chatService;

    @KafkaListener(topics = "chat-topic")
    public void consumeMessage(
            @Header("userNo") String userNo,
            @Payload ChatSaveRequest request
    ) {
        chatService.saveMessage(userNo, request);
    }
}