package uk.gov.dwp.jsa.jsaps.service;

import uk.gov.dwp.jsa.jsaps.model.es.Message;
import uk.gov.dwp.jsa.jsaps.model.es.Messages;
import uk.gov.dwp.jsa.jsaps.model.es.Result;
import uk.gov.dwp.jsa.jsaps.model.es.SystemErrors;

public class ESResultBuilder {
    private String completionIndicator = "true";
    private Messages messages = null;
    private SystemErrors systemErrors = null;


    public Result build() {
        final Result result = new Result();
        result.setCompletionIndicator(completionIndicator);
        result.setMessages(messages);
        result.setSystemErrors(systemErrors);
        return result;
    }

    public ESResultBuilder withCompletionIndicator(final String completionIndicator) {
        this.completionIndicator = completionIndicator;
        return this;
    }

    public ESResultBuilder withSystemError(final String systemError) {
        if (systemErrors == null) {
            systemErrors = new SystemErrors();
        }
        systemErrors.addSystemErrorItem(systemError);
        return this;
    }

    public ESResultBuilder withMessage(final String message) {
        if (messages == null) {
            messages = new Messages();
        }
        messages.addMessageItem(new Message().value(message));
        return this;
    }
}
