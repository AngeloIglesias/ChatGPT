package com.example.chatgpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Message {
    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private String content;
}