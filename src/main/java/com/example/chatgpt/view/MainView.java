package com.example.chatgpt.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.Optional;

//@CssImport(value = "./themes/chatgpt/styles.css")
//@JsModule("./styles/shared-styles.js")
public class MainView extends AppLayout {

    H2 viewTitle = new H2(""); //Not used here

    //Primary menu:
    private static final String appTitle = "OpenAI Tools";

    private final Tabs menu = createMenu();

    private final HorizontalLayout subMenu = createSubMenu();

    //Secondary menu:
    private final Component viewHeader = createHeaderContent();


    public MainView() {
        // Use the drawer for the menu
        setPrimarySection(Section.DRAWER);

        //Primary menu:
        addToDrawer(createDrawerContent( menu));

        //Secondary menu:
        addToNavbar(viewHeader);
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();

        // Configure styling for the drawer
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        // Have a drawer header with an application logo
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        //logoLayout.add(new Image("images/logo.png", "My Project logo"));
        H1 caption = new H1(appTitle);
        caption.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("line-height", "var(--lumo-size-l)")
                .set("margin", "0 var(--lumo-space-m)");
        logoLayout.add(caption);

        // Display the logo and the menu in the drawer
        layout.add(logoLayout, menu);
        return layout;
    }

    private Component createHeaderContent() {
        viewTitle.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        HorizontalLayout wrapper = new HorizontalLayout(new DrawerToggle(), subMenu);
        wrapper.setId("header");
        wrapper.getThemeList().set("dark", true);
        wrapper.setWidthFull();
        wrapper.setSpacing(false);
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);

        return wrapper;
    }

    private static Tabs createMenu() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.CHAT, "ChatGPT", ChatView.class),
                 createTab(VaadinIcon.WRENCH, "Settings", SettingsView.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.setSelectedIndex(1);

        tabs.setSelectedIndex(0); //Select first tab

        return tabs;
    }

    public void setSelectedTab(int index) {
        menu.setSelectedIndex(index);
    }

    protected static HorizontalLayout createSubMenu() {
        return new HorizontalLayout();
    }

    protected void setSubMenu(Component subMenu) {
        this.subMenu.removeAll();
        this.subMenu.add(subMenu);
    }

    private static Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> view) {
        Icon icon = viewIcon.create();

        //Desktop:
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(view);
        link.setTabIndex(-1);

        return new Tab(link);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Select the tab corresponding to currently shown view
//        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);

        // Set the view title in the header
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class)
                        .equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }
}