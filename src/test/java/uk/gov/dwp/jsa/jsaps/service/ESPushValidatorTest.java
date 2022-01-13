package uk.gov.dwp.jsa.jsaps.service;

import org.junit.Test;
import uk.gov.dwp.jsa.jsaps.model.es.Result;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ESPushValidatorTest {

    private ESPushValidator validator;
    private boolean isSuccessful;

    @Test
    public void returnsSuccessful() {
        givenAValidator();
        whenIValidateResult(new ESResultBuilder().build());
        thenTheResultIsSuccessful();
    }

    @Test
    public void returnsTrueIfCompletionIndicatorIsTrue() {
        givenAValidator();
        whenIValidateResult(new ESResultBuilder().withCompletionIndicator("true").build());
        thenTheResultIsSuccessful();
    }

    @Test
    public void returnsFalseIfCompletionIndicatorIsFalse() {
        givenAValidator();
        whenIValidateResult(new ESResultBuilder().withCompletionIndicator("false").build());
        thenTheResultIsUnsuccessful();
    }

    @Test
    public void returnsFalseIfMessagesHasError() {
        givenAValidator();
        whenIValidateResult(new ESResultBuilder().withMessage("String containing Error").build());
        thenTheResultIsUnsuccessful();
    }

    @Test
    public void returnsTrueIfMessagesEmpty() {
        givenAValidator();
        whenIValidateResult(new ESResultBuilder().build());
        thenTheResultIsSuccessful();
    }

    @Test
    public void returnsTrueIfSystemErrorsEmpty() {
        givenAValidator();
        whenIValidateResult(new ESResultBuilder().build());
        thenTheResultIsSuccessful();
    }

    @Test
    public void returnsFalseIfSystemErrors() {
        givenAValidator();
        whenIValidateResult(new ESResultBuilder().withSystemError("Any value").build());
        thenTheResultIsUnsuccessful();
    }

    private void givenAValidator() {
        validator = new ESPushValidator();
    }

    private void whenIValidateResult(final Result result) {
        isSuccessful = validator.isSuccessful(result);
    }

    private void thenTheResultIsSuccessful() {
        assertThat(isSuccessful, is(true));
    }

    private void thenTheResultIsUnsuccessful() {
        assertThat(isSuccessful, is(false));
    }

}
