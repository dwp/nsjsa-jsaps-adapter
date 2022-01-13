package uk.gov.dwp.jsa.jsaps.service;

import java.util.List;

public class JsapsServiceResult {

    private Boolean success;

    private List<String>  messages;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(final Boolean success) {
        this.success = success;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(final List<String> messages) {
        this.messages = messages;
    }
}
