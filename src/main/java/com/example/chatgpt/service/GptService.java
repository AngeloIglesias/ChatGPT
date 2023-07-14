package com.example.chatgpt.service;

import com.example.chatgpt.model.ChatRequest;
import com.example.chatgpt.model.ChatResponse;
import com.example.chatgpt.model.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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

//    private WebClient webClient = WebClient.create();
    WebClient webClient = WebClient.create(OPENAI_BASE_PATH);

    public ChatResponse sendMessage(String message) {
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

        List<Message> messages = List.of(message1);

        ChatRequest request = new ChatRequest();
        request.setMessages(messages);

        //ToDo
// populate the request object


        ChatResponse response = webClient.post()
                .uri(CHAT_PATH)
                .header("Authorization", "Bearer " + apiKey)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        return response;
    }


    @PostConstruct
    private void postConstruct() {
        System.out.println("------------------- API Key -> " + apiKey);
    }
}
