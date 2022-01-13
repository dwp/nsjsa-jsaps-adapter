package uk.gov.dwp.jsa.jsaps.service;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import uk.gov.dwp.jsa.jsaps.model.es.Result;

@Component
public class ESPushValidator {
    public boolean isSuccessful(final Result result) {
        return hasSuccessfulCompletionIndicator(result)
                && hasSuccessfulSystemErrors(result)
                && hasSuccessfulMessages(result);
    }

    private boolean hasSuccessfulMessages(final Result result) {
        if (result.getMessages() == null || result.getMessages().getMessage() == null) {
            return true;
        }
        return result.getMessages().getMessage().stream().noneMatch(e -> e.getValue().toUpperCase().contains("ERROR"));
    }

    private boolean hasSuccessfulSystemErrors(final Result result) {
        if (result.getSystemErrors() == null || result.getSystemErrors().getSystemError() == null) {
            return true;
        }
        return result.getSystemErrors().getSystemError().isEmpty();
    }

    private boolean hasSuccessfulCompletionIndicator(final Result result) {
        return BooleanUtils.toBoolean(result.getCompletionIndicator());
    }

}
