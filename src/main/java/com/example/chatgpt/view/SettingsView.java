package com.example.chatgpt.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Settings")
@Route(value = "settings", layout = MainView.class)
//@CssImport(value = "./themes/chatgpt/styles.css")
public class SettingsView extends VerticalLayout {

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
