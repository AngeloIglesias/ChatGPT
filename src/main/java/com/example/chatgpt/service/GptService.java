package com.example.chatgpt.service;

import com.example.chatgpt.model.ChatRequest;
import com.example.chatgpt.model.ChatResponse;
import com.example.chatgpt.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.messages.MessageListItem;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class GptService {

    private static final String OPENAI_BASE_PATH = "https://api.openai.com";
    private static final String CHAT_PATH = "/v1/chat/completions";

    @Value("${apikey}")
    private String apiKey;

    private ObjectMapper objectMapper;
    private final List<Message> messages = new ArrayList<>(); // Speichert den gesamten Chatverlauf

    WebClient webClient = WebClient.create(OPENAI_BASE_PATH);

    public GptService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Mono<String> sendMessage(String message) {

        Message userMessage = new Message();
        userMessage.setContent(message);
        userMessage.setRole("user");

        messages.add(userMessage); // Fügt die neue Benutzernachricht zur Liste hinzu

        ChatRequest request = new ChatRequest();
        request.setModel("gpt-3.5-turbo");
        request.setMessages(messages);

        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Serialization failed", e);  //ToDo
        }
        System.out.println( "---------------------- REQUEST --> " + requestJson);

        Mono<String> response = webClient.post()
                .uri(CHAT_PATH)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(requestJson))
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .map(chatResponse -> chatResponse.getChoices().get(0).getMessage().getContent())
                .doOnNext(content -> {
                    // Seiteneffekt: füge Inhalt zur Nachrichtenliste hinzu
                    Message newMessage = new Message();
                    newMessage.setContent(content);
                    newMessage.setRole("system");
                    messages.add(newMessage);
                });

        return response;
    }


    @PostConstruct
    private void postConstruct() {

        //ToDo: Better remove this!
        System.out.println("------------------- API Key -> " + apiKey);
    }
}
