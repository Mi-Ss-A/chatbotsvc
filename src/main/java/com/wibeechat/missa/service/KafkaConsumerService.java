package com.wibeechat.missa.service;

import com.wibeechat.missa.dto.ChatSaveRequest;
import com.wibeechat.missa.dto.ChatSaveResponse;
import com.wibeechat.missa.dto.MessageDto;
import com.wibeechat.missa.entity.ChatMessage;
import com.wibeechat.missa.repostitory.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// Consumer Service 수정
@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaConsumerService {
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    @KafkaListener(topics = "your-topic-name", groupId = "my-group")
    public ChatSaveResponse saveMessage(String userNo, ChatSaveRequest request) {
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

        return ChatSaveResponse.from(savedMessage);
    }
}