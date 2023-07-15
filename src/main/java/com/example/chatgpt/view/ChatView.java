package com.example.chatgpt.view;

import com.example.chatgpt.service.GptService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

//@Route(value = "chat")
@Route(value = "")
@UIScope
@CssImport("./styles/styles.css")
public class ChatView extends VerticalLayout {

    private final GptService gptService;
    private final MessageList messageList = new MessageList();
    private final TextField textField = new TextField();
    private final List<MessageListItem> messages = new ArrayList<>();

    private UI current;

    @Autowired
    private ZoneOffset zoneOffset;

    public ChatView(GptService gptService) {
        current = UI.getCurrent();
        this.gptService = gptService;

        messageList.addClassName("chat-message-list");

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
        MessageListItem userMessage = new MessageListItem(message, LocalDateTime.now().toInstant(zoneOffset), "You");
        userMessage.addThemeNames("current-user");
        userMessage.addThemeNames("user");
        messages.add(userMessage);
        messageList.setItems(messages);
        gptService.sendMessage(message)
                .subscribe(this::handleResponse);
    }

    private void handleResponse(String response) {
        // parse the response to get the actual message
        MessageListItem botMessage = new MessageListItem(response, LocalDateTime.now().toInstant(zoneOffset), "Bot");
        botMessage.addThemeNames("bot");
        botMessage.addThemeNames("current-bot");
        messages.add(botMessage);

        //add to ui
        current.access(() -> {
            messageList.setItems(messages);
            current.push();
        });
    }
}
