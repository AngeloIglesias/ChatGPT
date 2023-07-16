package com.example.chatgpt.view;

import com.example.chatgpt.view.webcomponents.PrismWrapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.textfield.TextField;

import java.awt.*;

@PageTitle("Settings")
@Route(value = "settings", layout = MainView.class)
//@PWA(name = "Ebook Scraper", shortName = "Scraper")
//@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport(value = "./themes/chatgpt/styles.css")
@JsModule("./themes/chatgpt/components/prism-wrapper.js")
public class SettingsView extends VerticalLayout {

    public SettingsView()
    {
//        getThemeList().set("dark", true);

        TextField label = new TextField("--- CONSTRUCTION SITE ---");
        add(label);

        /////////////////////////////////////
        PrismWrapper prismWrapper = new PrismWrapper();
        prismWrapper.setCode("public static void main(String[] args) { ... }");
        prismWrapper.setLanguage("java");


        add(prismWrapper);
        /////////////////////////////////////

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
