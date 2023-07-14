package com.example.chatgpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Choice {
    @JsonProperty("message")
    private ResponseMessage message;

    @JsonProperty("finish_reason")
    private String finishReason;

}
