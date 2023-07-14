package com.example.chatgpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatRequest {
    @JsonProperty("model")
    private String model;

    @JsonProperty("messages")
    private List<Message> messages;
}