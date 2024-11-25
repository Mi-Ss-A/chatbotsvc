package com.wibeechat.missa.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wibeechat.missa.dto.ChatSaveRequest;
import com.wibeechat.missa.dto.ChatSaveResponse;
import com.wibeechat.missa.entity.ChatMessage;
import com.wibeechat.missa.repostitory.ChatMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaConsumerService {
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatSaveResponse saveMessage(String userNo, ChatSaveRequest request) {
        try {
            ChatMessage message = ChatMessage.builder()
                    .userNo(userNo)
                    .message(ChatMessage.Message.builder()
                            .sender(ChatMessage.SenderType.valueOf(request.getSender()))
                            .content(request.getContent())
                            .timestamp(LocalDateTime.now())
                            .build())
                    .metadata(ChatMessage.MessageMetadata.builder()
                            .messageLength(request.getContent().length())
                            .build())
                    .status(ChatMessage.MessageStatus.builder()
                            .isProcessed(ChatMessage.SenderType.valueOf(request.getSender()) == ChatMessage.SenderType.AI)
                            .build())
                    .build();

            ChatMessage savedMessage = chatMessageRepository.save(message);
            log.info("Message saved successfully for userNo: {}", userNo);
            return ChatSaveResponse.from(savedMessage);
        } catch (Exception e) {
            log.error("Error saving message for userNo {}: {}", userNo, e.getMessage(), e);
            throw e;
        }
    }
}