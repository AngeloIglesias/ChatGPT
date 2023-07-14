package com.example.chatgpt.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GptService {

//    @Value("${apikey}")

//    @Value("${config.openai.apikey}")
    @Value("${OpenAI}")
    private String apiKey;

    private static final String GPT_API = "https://api.openai.com/v1/engines/davinci-codex/completions";
//    private static final String API_KEY = "";

    private WebClient webClient = WebClient.create();

    public Mono<String> sendMessage(String message) {
        return webClient.post()
                .uri(GPT_API)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue("{\"prompt\":\""+message+"\",\"max_tokens\":60}"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

    @PostConstruct
    private void postConstruct() {
        System.out.println("------------------- API Key -> " + apiKey);
    }
}
