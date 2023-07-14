package com.example.chatgpt.service;

import com.example.chatgpt.model.ChatRequest;
import com.example.chatgpt.model.ChatResponse;
import com.example.chatgpt.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GptService {

    private static final String OPENAI_BASE_PATH = "https://api.openai.com";
    private static final String CHAT_PATH = "/v1/chat/completions";

    @Value("${apikey}")
    private String apiKey;

    private ObjectMapper objectMapper;

//    private WebClient webClient = WebClient.create();
    WebClient webClient = WebClient.create(OPENAI_BASE_PATH);

    public GptService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Mono<ChatResponse> sendMessage(String message) {
        /*   return webClient.post()
                .uri(GPT_API)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"prompt\":\""+message+"\",\"max_tokens\":60}"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }*/

        Message message1 = new Message();
        message1.setContent(message);
        message1.setRole("user");  //user or system?

        List<Message> messages = List.of(message1);

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

        Mono<ChatResponse> response = webClient.post()
                .uri(CHAT_PATH)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(requestJson))
                .retrieve()
                .bodyToMono(ChatResponse.class);

        return response;
    }


    @PostConstruct
    private void postConstruct() {

        //ToDo: Better remove this!
        System.out.println("------------------- API Key -> " + apiKey);
    }
}
