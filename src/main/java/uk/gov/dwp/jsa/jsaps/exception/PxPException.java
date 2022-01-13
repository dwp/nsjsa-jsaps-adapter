package uk.gov.dwp.jsa.jsaps.exception;

import java.util.List;

public class PxPException extends RuntimeException {

    private List<String> messages;

    public PxPException() {
    }

    public PxPException(final List<String> messages) {
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(final List<String> messages) {
        this.messages = messages;
    }
}
