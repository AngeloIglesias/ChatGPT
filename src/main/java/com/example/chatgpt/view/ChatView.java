package com.example.chatgpt.view;

import com.example.chatgpt.model.ChatResponse;
import com.example.chatgpt.service.GptService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Route(value = "chat")
@UIScope
public class ChatView extends VerticalLayout {

    private final GptService gptService;
    private final MessageList messageList = new MessageList();
    private final TextField textField = new TextField();
    private final List<MessageListItem> messages = new ArrayList<>();

    @Autowired
    private ZoneOffset zoneOffset;

    public ChatView(GptService gptService) {
        this.gptService = gptService;
        add(messageList, createInputLayout());
    }

    private HorizontalLayout createInputLayout() {
        Button sendButton = new Button("Send", event -> sendMessage());
        sendButton.setThemeName("primary");
        HorizontalLayout inputLayout = new HorizontalLayout(textField, sendButton);
        inputLayout.expand(textField);
        return inputLayout;
    }

    private void sendMessage() {
        String message = textField.getValue();
        textField.clear();
        messages.add(new MessageListItem(message, LocalDateTime.now().toInstant(zoneOffset), "You"));
        messageList.setItems(messages);
        gptService.sendMessage(message)
                .subscribe(this::handleResponse);
    }

    private void handleResponse(ChatResponse response) {
        // parse the response to get the actual message
        messages.add(new MessageListItem(response.getChoices().get(0).getMessage().getContent(), LocalDateTime.now().toInstant(ZoneOffset.UTC), "Bot"));

        //add to ui
        VaadinSession.getCurrent().lock();
        try {
            UI.getCurrent().access(() -> {
                messageList.setItems(messages);
            });
        } finally {
            VaadinSession.getCurrent().unlock();
        }
    }
}
