package com.example.chatgpt.view;

import com.example.chatgpt.service.GptService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageInputI18n;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

//@Route(value = "chat")
@PageTitle("ChatGPT")
@RouteAlias(value = "", layout = MainView.class)
@Route(value = "chat", layout = MainView.class)
@UIScope
//@CssImport("./themes/chatgpt/styles.css")
public class ChatView extends VerticalLayout {

    private final GptService gptService;
    private final MessageList messageList = new MessageList();
    private final MessageInput messageInput = new MessageInput();
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
        HorizontalLayout inputLayout = new HorizontalLayout();
        inputLayout.getThemeList().set("dark", true);

        MessageInputI18n messageInputI18n = new MessageInputI18n();
        messageInputI18n.setMessage("Send a message");
        messageInputI18n.setSend("➢");

        messageInput.setI18n(messageInputI18n);
        messageInput.setClassName("message");

        messageInput.addSubmitListener(this::sendMessage);

        inputLayout = new HorizontalLayout();
        inputLayout.add(messageInput);
        inputLayout.setFlexGrow(1, messageInput); //Set Component which scales to window size
        inputLayout.setWidthFull();

        return inputLayout;
    }

    private void sendMessage(MessageInput.SubmitEvent sendMessage) {
        String message = sendMessage.getValue();

        MessageListItem userMessage = new MessageListItem(message, LocalDateTime.now().toInstant(zoneOffset), "You");
        userMessage.addThemeNames("user-message");
        userMessage.setUserColorIndex(5);

        messages.add(userMessage);
        messageList.setItems(messages);
        gptService.sendMessage(message)
                .subscribe(this::handleResponse);
    }

    private void handleResponse(String response) {
        // parse the response to get the actual message
        MessageListItem botMessage = new MessageListItem(response, LocalDateTime.now().toInstant(zoneOffset), "Bot");
        botMessage.addThemeNames("bot-message");
        botMessage.setUserColorIndex(6);

        messages.add(botMessage);

        //add to ui
        current.access(() -> {
            messageList.setItems(messages);
            current.push();
        });
    }

    private String extractCodeBlock(String codeBlock)
    {
        String language = "Java"; //ToDo
        int start = codeBlock.indexOf("```" + language) + 3;
        int end = codeBlock.indexOf("```", start);
        return codeBlock.substring(start, end).trim();
    }
}
