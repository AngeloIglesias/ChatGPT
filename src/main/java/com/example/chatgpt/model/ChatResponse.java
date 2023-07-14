package com.example.chatgpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created")
    private long created;

    @JsonProperty("choices")
    private List<Choice> choices;

    @JsonProperty("usage")
    private Usage usage;
}
