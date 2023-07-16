package com.example.chatgpt.view;

import com.example.chatgpt.service.GptService;
import com.example.chatgpt.view.webcomponents.PrismWrapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

//@Route(value = "chat")
@PageTitle("ChatGPT")
@RouteAlias(value = "", layout = MainView.class)
@Route(value = "chat", layout = MainView.class)
@UIScope
@CssImport("./themes/chatgpt/styles.css")
@JsModule("./themes/chatgpt/components/prism-wrapper.js")
public class ChatView extends VerticalLayout {

    private final GptService gptService;
    private final MessageList messageList = new MessageList();
    private final TextArea textField = new TextArea();
    private final List<MessageListItem> messages = new ArrayList<>();

    private UI current;

    @Autowired
    private ZoneOffset zoneOffset;


    public ChatView(GptService gptService) {
//        getThemeList().set("dark", true);
        current = UI.getCurrent();
        this.gptService = gptService;

        messageList.addClassName("chat-message-list");

        add(messageList, createInputLayout());


        /////////////////////////////////////
        PrismWrapper prismWrapper = new PrismWrapper();
        prismWrapper.setCode("public static void main(String[] args) { ... }");
        prismWrapper.setLanguage("java");
        add(prismWrapper);
        /////////////////////////////////////
    }

    private HorizontalLayout createInputLayout() {
        HorizontalLayout inputLayout = new HorizontalLayout();
        inputLayout.getThemeList().set("dark", true);

        Button sendButton = new Button("➢", event -> sendMessage()); //alternative: ⊳ or ⩥
        sendButton.setWidth("100px");
        sendButton.setClassName("send");
        sendButton.setThemeName("primary");

        textField.setClassName("message");

        inputLayout = new HorizontalLayout(textField, sendButton);
//        inputLayout.expand(textField);
        inputLayout.add(textField, sendButton);
        inputLayout.setFlexGrow(1, textField); //Set Component which scales to window size
        inputLayout.setWidthFull();

        return inputLayout;
    }

    private void sendMessage() {
        String message = textField.getValue();
        textField.clear();
        MessageListItem userMessage = new MessageListItem(message, LocalDateTime.now().toInstant(zoneOffset), "You");
        userMessage.addThemeNames("user-message");
        messages.add(userMessage);
        messageList.setItems(messages);
        gptService.sendMessage(message)
                .subscribe(this::handleResponse);
    }

    private void handleResponse(String response) {
        // parse the response to get the actual message
        MessageListItem botMessage = new MessageListItem(response, LocalDateTime.now().toInstant(zoneOffset), "Bot");
        botMessage.addThemeNames("bot-message");
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
