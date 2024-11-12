package com.wibeechat.missa.controller;

import com.wibeechat.missa.annotation.CurrentUser;
import com.wibeechat.missa.annotation.LoginRequired;
import com.wibeechat.missa.dto.ChatSaveRequest;
import com.wibeechat.missa.dto.ChatSaveResponse;
import com.wibeechat.missa.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    @LoginRequired
    public ResponseEntity<ChatSaveResponse> saveMessage(
            @CurrentUser String userId,  // 세션에서 자동으로 userId를 가져옴
            @RequestBody ChatSaveRequest request
    ) {
        return ResponseEntity.ok(chatService.saveMessage(userId, request));
    }
}