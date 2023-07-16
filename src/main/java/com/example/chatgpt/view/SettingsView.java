package com.example.chatgpt.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.Optional;

@PageTitle("Settings")
@Route(value = "settings", layout = MainView.class)
//@CssImport(value = "./themes/chatgpt/styles.css")
public class SettingsView extends VerticalLayout implements AfterNavigationObserver {

    private Tabs tabs;

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        Optional<Component> mainViewOptional = UI.getCurrent().getChildren()
                .filter(component -> component.getClass() == MainView.class)
                .findFirst();

        if (mainViewOptional.isPresent()) {
            MainView mainView = (MainView) mainViewOptional.get();
            // Jetzt kannst du auf die Methoden von MainView zugreifen, z.B.:
            mainView.setSubMenu(createSubMenu());
        } else {
            // Behandlung, wenn MainView nicht gefunden wurde.
            Notification.show("ERROR: MainView not found.", 3000, Notification.Position.MIDDLE);
        }
    }

    private Component createSubMenu() {
        tabs = new Tabs();

        return tabs;
    }

    public SettingsView()
    {
        //ToDo
    }


//ToDo: Not Working, use method with same name in MainView
//    protected Component createSubMenu() {
//        Tabs tabs = new Tabs();
//        tabs.add(new Tab("General"), new Tab("ChatGPT"));
//
//        //For Mobile Phones:
//        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL,
//                TabsVariant.LUMO_EQUAL_WIDTH_TABS);
//
//        return tabs;
//    }
}
