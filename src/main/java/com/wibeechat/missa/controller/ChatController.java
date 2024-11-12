package com.wibeechat.missa.controller;

import com.wibeechat.missa.annotation.CurrentUser;
import com.wibeechat.missa.annotation.LoginRequired;
import com.wibeechat.missa.dto.ChatSaveRequest;
import com.wibeechat.missa.dto.ChatSaveResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final KafkaTemplate<String, ChatSaveRequest> kafkaTemplate;

    @PostMapping
    @LoginRequired
    public ResponseEntity<ChatSaveResponse> saveMessage(
            @CurrentUser String userno,
            @RequestBody ChatSaveRequest request
    ) {
        // ProducerRecord를 사용하여 헤더와 함께 메시지 전송
        ProducerRecord<String, ChatSaveRequest> record =
                new ProducerRecord<>("chat-topic", request);
        record.headers().add("userNo", userno.getBytes());

        kafkaTemplate.send(record);
        return ResponseEntity.accepted().build();
    }
}