package com.example.chatgpt.view.webcomponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;

@Tag("prism-wrapper")
@JsModule("./themes/chatgpt/components/prism-wrapper.js")
public class PrismWrapper extends Component {

    public PrismWrapper() {
    }

    public void setCode(String code) {
        getElement().setProperty("code", code);
    }

    public void setLanguage(String language) {
        getElement().setProperty("language", language);
    }
}
