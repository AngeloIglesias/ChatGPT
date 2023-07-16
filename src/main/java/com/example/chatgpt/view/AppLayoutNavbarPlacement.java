package com.example.chatgpt.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

@Route("navbar") // Map the view to the root URL
@CssImport(value = "./themes/chatgpt/styles.css")
public class AppLayoutNavbarPlacement extends AppLayout {

    public AppLayoutNavbarPlacement() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("MyApp");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Tabs tabs = getTabs();

        addToDrawer(tabs);
        addToNavbar(toggle, title);
    }


    private Tabs getTabs() {
        Tab booksTab = new Tab("Books");
        Tab downloadTab = new Tab("Download");

        Tabs tabs = new Tabs(booksTab, downloadTab);
        return tabs;
    }
}