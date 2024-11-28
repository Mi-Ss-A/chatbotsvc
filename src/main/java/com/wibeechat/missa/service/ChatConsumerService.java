package com.wibeechat.missa.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibeechat.missa.dto.ChatSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
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
                    .sessionId(messageMap.get("sessionId"))
                    .build();

            String sessionId = messageMap.get("sessionId");
            //해당 세션아이디로 레디스에서 userno조회 해오기
            String userNo = redisTemplate.opsForValue().get("session:user:" + sessionId);
            userNo = userNo.replace("\"", "");

            chatService.saveMessage(userNo, request);
            log.info("Message processed successfully");

        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}

