package com.example.chatgpt.view;

import com.example.chatgpt.service.GptService;
import com.sun.jna.platform.win32.Netapi32Util;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.html.Aside;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageInputI18n;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import com.vaadin.flow.router.internal.RouteTarget;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@Route(value = "chat")
@PageTitle("ChatGPT")
@RouteAlias(value = "", layout = MainView.class)
@Route(value = "chat", layout = MainView.class)
@UIScope
//@CssImport("./themes/chatgpt/styles.css")
public class ChatView extends VerticalLayout implements AfterNavigationObserver {

    private ChatInfo[] chats = new ChatInfo[]{new ChatInfo("1", 0)};
    private ChatInfo currentChat = chats[0];
    private Tabs tabs;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final GptService gptService;
    private final MessageList messageList = new MessageList();
    private final MessageInput messageInput = new MessageInput();
    private final List<MessageListItem> messages = new ArrayList<>();

    private UI current;

    @Autowired
    private ZoneOffset zoneOffset;

    /**
     * Callback executed before navigation to attaching Component chain is made.
     *
     * @param event before navigation event with event details
     */
    @Override
    public void afterNavigation(AfterNavigationEvent  event) {
        Optional<Component> mainViewOptional = UI.getCurrent().getChildren()
                .filter(component -> component.getClass() == MainView.class)
                .findFirst();

        if (mainViewOptional.isPresent()) {
            MainView mainView = (MainView) mainViewOptional.get();

            //Access MainView:
//            mainView.setSelectedTab(0);
            mainView.setSubMenu(createSubMenu());
        } else {
            // Behandlung, wenn MainView nicht gefunden wurde.
            Notification.show("ERROR: MainView not found.", 3000, Notification.Position.MIDDLE);
        }
    }


    public ChatView(GptService gptService) {
        current = UI.getCurrent();
        this.gptService = gptService;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        addClassNames("chat-view", LumoUtility.Width.FULL, LumoUtility.Display.FLEX, LumoUtility.Flex.AUTO);
        setSpacing(false);

        // extra layouting
        /*
        VerticalLayout chatContainer = new VerticalLayout();
        chatContainer.addClassNames(LumoUtility.Flex.AUTO, LumoUtility.Overflow.HIDDEN);

        Aside side = new Aside();
        side.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Flex.GROW_NONE, LumoUtility.Flex.SHRINK_NONE, LumoUtility.Background.CONTRAST_5);
        side.setWidth("18rem");

        Header header = new Header();
        header.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW, LumoUtility.Width.FULL, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxSizing.BORDER);
        H3 channels = new H3("Channels");
        channels.addClassNames(LumoUtility.Flex.GROW, LumoUtility.Margin.NONE);

        AvatarGroup avatarGroup = new AvatarGroup();
        avatarGroup.setMaxItemsVisible(4);
        avatarGroup.addClassNames(LumoUtility.Width.AUTO);

        header.add(channels, avatarGroup);

        side.add(header, tabs);

        chatContainer.add(messageList, createInputLayout());
        add(side, chatContainer);*/

        add(messageList, createInputLayout());

        setSizeFull();
        expand(messageList);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

        messageList.addClassName("chat-message-list");

//        current = UI.getCurrent();
//        this.gptService = gptService;
//
//
//
//        add(messageList, createInputLayout());
    }

    private Component createSubMenu() {
        tabs = new Tabs();
        for (ChatInfo chat : chats) {

            tabs.add(createTab(chat));
        }
        tabs.add(createAddTab()); //ToDo

        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.addClassNames(LumoUtility.Flex.GROW, LumoUtility.Flex.SHRINK, LumoUtility.Overflow.HIDDEN);

        // When a new tab is selected..
        tabs.addSelectedChangeListener(event -> {
            currentChat = ((ChatTab) event.getSelectedTab()).getChatInfo();
            currentChat.resetUnread();
        });

        return tabs;
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private ChatTab createTab(ChatInfo chat) {
        ChatTab tab = new ChatTab(chat);
        tab.addClassNames(LumoUtility.JustifyContent.BETWEEN);

        Span badge = new Span();
        chat.setUnreadBadge(badge);
        badge.getElement().getThemeList().add("badge small contrast");
        tab.add(new Span("#" + chat.name), badge);

        return tab;
    }

    private Tab createAddTab() {
        VaadinIcon viewIcon = VaadinIcon.PLUS;
        Icon icon = viewIcon.create();

        //Desktop:
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon);
        link.setRoute(ChatView.class);
        link.setTabIndex(-1);

        Tab tab = new Tab(link);

        tab.addClassNames(LumoUtility.JustifyContent.BETWEEN);

        Span badge = new Span();
        badge.getElement().getThemeList().add("badge small contrast");

        return tab;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class ChatTab extends Tab {
        private final ChatInfo chatInfo;

        public ChatTab(ChatInfo chatInfo) {
            this.chatInfo = chatInfo;
        }

        public ChatInfo getChatInfo() {
            return chatInfo;
        }
    }

    public static class ChatInfo {
        private String name;
        private int unread;
        private Span unreadBadge;

        private ChatInfo(String name, int unread) {
            this.name = name;
            this.unread = unread;
        }

        public void resetUnread() {
            unread = 0;
            updateBadge();
        }

        public void incrementUnread() {
            unread++;
            updateBadge();
        }

        private void updateBadge() {
            unreadBadge.setText(unread + "");
            unreadBadge.setVisible(unread != 0);
        }

        public void setUnreadBadge(Span unreadBadge) {
            this.unreadBadge = unreadBadge;
            updateBadge();
        }

        public String getCollaborationTopic() {
            return "chat/" + name;
        }
    }
}
